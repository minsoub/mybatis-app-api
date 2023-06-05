package kr.co.fns.app.api.fanit.service;


import kr.co.fns.app.api.country.entity.CountryData;
import kr.co.fns.app.api.country.mapper.CountryMapper;
import kr.co.fns.app.api.fanit.entity.FanitList;
import kr.co.fns.app.api.fanit.entity.FanitListDetail;
import kr.co.fns.app.api.fanit.entity.FanitPoint;
import kr.co.fns.app.api.fanit.mapper.FanitMapper;
import kr.co.fns.app.api.fanit.model.request.FanitPointInsertReq;
import kr.co.fns.app.api.fanit.model.response.FanitListDetailResponse;
import kr.co.fns.app.api.fanit.model.response.FanitListResponse;
import kr.co.fns.app.api.fanit.model.response.FanitMessageResponse;
import kr.co.fns.app.api.fanit.model.response.FanitPointResponse;
import kr.co.fns.app.base.service.CheckBaseService;
import kr.co.fns.app.base.service.MessageSourceService;
import kr.co.fns.app.config.resolver.Account;
import kr.co.fns.app.core.aop.exception.ErrorCode;
import kr.co.fns.app.core.aop.exception.custom.FantooException;
import kr.co.fns.app.core.receiver.FanitData;
import kr.co.fns.app.core.receiver.enums.RewardType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@AllArgsConstructor
public class FanitService extends CheckBaseService{

    @Autowired
    MessageSourceService me;

    private FanitMapper fanitMapper;

    private CountryMapper countryMapper;

    //일일 사용가능한 포인트
    private static final int perAblePoint = 1000000;
    //아모페 한번 가능한 포인트
    private static final int amofeAblePoint = 10;

    //해당 모델 하루 투표 횟수 50번 이상은 안됨 49번까지 가능
    private static final int amofeAbleCnt = 50;

    //로그인 리워드 하루 적립 포인트
    private static final int saveDayFanitPoint = 10;

    //아모페 리워드 하루 적립 포인트
    private static final int saveDayAmofePoint = 50;

    @Transactional(readOnly = true)
    public FanitListResponse getFanitPro(Integer page) {

        int pageSize = 10;
        page = (ObjectUtils.isEmpty(page) || (page < 1)) ? 1 : page;
        int startRow = ((page - 1) * pageSize);
        List<FanitList> fanitList = fanitMapper.getFanitPro(pageSize,startRow);

        return FanitListResponse.builder()
                .fanitList(fanitList)
                .build();
    }

    @Transactional
    public String saveFanit(Account account, String integUid, RewardType rewardType, String refId){
        String result = "";
        int insertResult = 0;
        String userIp = account.getUserIp();
        String ip = this.extractIpFromRequest(userIp);
        CountryData countryData = countryMapper.getIpNation(ip);
        String country = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountry();
        String countryCode = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountryCode();
        FanitData fanitData = FanitData.builder()
                .rewardType(rewardType)
                .integUid(integUid)
                .refId(refId)
                .type("1")
                .userIp(account.getUserIp())
                .country(country)
                .countryCode(countryCode)
                .build();
        FanitPointInsertReq fanitPointInsertReq = new FanitPointInsertReq(rewardType,fanitData);
        Boolean isFanitPayment = fanitMapper.isRewardFanitPayment(fanitPointInsertReq);

        if(!isFanitPayment){
            insertResult = fanitMapper.insertFanitPoint(fanitPointInsertReq);
        }
        if(insertResult > 0){
            result = "적립성공";
        }else{
            throw new FantooException(ErrorCode.ERROR_FE5015);
        }
        return result;
    }

    @Transactional(readOnly = true)
    public FanitListDetailResponse getFanitDetail(int idx) {

        FanitList fanitDetail = fanitMapper.getDetail(idx);
        if (!ObjectUtils.isEmpty(fanitDetail)) {
            List<FanitListDetail> fanitListDetail = new ArrayList<>();
            int totalVoteCnt = fanitDetail.getTotalVoteCnt();
            fanitListDetail = fanitMapper.getListDetail(idx, totalVoteCnt);
            fanitDetail.setFanitListDetail(fanitListDetail);
        }
        return FanitListDetailResponse.builder()
                .fanitDetail(fanitDetail)
                .build();
    }

    @Transactional
    public FanitListDetailResponse updateFanit(int idx) {
        FanitList fanitDetail = fanitMapper.getModifyFanitList(idx);

        if (ObjectUtils.isEmpty(fanitDetail)) {
            throw new FantooException(ErrorCode.ERROR_FE5001);
        }

        int totalVoteCnt = fanitDetail.getTotalVoteCnt();
        List<FanitListDetail> fanitListDetail = fanitMapper.getFanitDetails(idx,totalVoteCnt);
        fanitListDetail.forEach(s -> fanitMapper.modifyFanitListDetail(s.getFanitDetailId(),s.getVoteCnt()));

        fanitDetail.setFanitListDetail(fanitListDetail);
        return FanitListDetailResponse.builder()
                .fanitDetail(fanitDetail)
                .build();
    }

    @Transactional
    public FanitListResponse updateFanitList() {
        List<FanitList> fanitList = fanitMapper.getUpdateFanitList();
        fanitList.forEach(s -> updateFanit(s.getIdx()));

        return FanitListResponse.builder()
                .fanitList(fanitList)
                .build();
    }

    @Transactional
    public FanitMessageResponse updateFanitVote(Account account, String integUid, int idx, int fanitDetailId, int point) {
        FanitMessageResponse fanitMessageResponse = new FanitMessageResponse();
        this.uidCheck(account.getIntegUid(),integUid);
        if(point < 0){
            throw new FantooException(ErrorCode.ERROR_FE5005);
        }
        Boolean isFanitPointUse = fanitMapper.isFanitPointUse(integUid,point);

        if(!isFanitPointUse){
            throw new FantooException(ErrorCode.ERROR_FE5004,String.valueOf(fanitMapper.getFanitUseAblePoint(integUid,false)));
        }else{
            //오늘 사용했던 포인트 (음수)
            int todayUsePoint = fanitMapper.getFanitUseAblePoint(integUid,true);
            //오늘 사용했던 포인트 + 팬잇투표로 사용할 포인트
            int todayPlanPoint = (-1 * todayUsePoint) + point;
            //오늘 사용이 가능한 포인트
            int remindPoint = perAblePoint + todayUsePoint;
            remindPoint = (remindPoint < 0) ? 0 : remindPoint;
            if(todayPlanPoint > perAblePoint){
                fanitMessageResponse.setRemindPoint(remindPoint);
                throw new FantooException(ErrorCode.ERROR_FE5005);
            }else{
                Boolean isFanitListDetail = fanitMapper.isFanitListDetail(fanitDetailId);
                if(isFanitListDetail){
                    String userIp = account.getUserIp();
                    String ip = this.extractIpFromRequest(userIp);
                    CountryData countryData = countryMapper.getIpNation(ip);
                    String country = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountry();
                    String countryCode = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountryCode();
                    fanitMapper.insertFanitPoint(FanitPointInsertReq.builder()
                            .type("0")
                            .fanitId(idx)
                            .country(country)
                            .countryCode(countryCode)
                            .fanitDetailId(fanitDetailId)
                            .integUid(integUid)
                            .point(point * -1)
                            .userIp(userIp)
                            .build());
                    fanitMapper.modifyFanitListDetail(fanitDetailId,point);
                    fanitMessageResponse.setPoint(fanitMapper.getFanitUseAblePoint(integUid,false));
                    fanitMessageResponse.setUsepoint(point);
                }else{
                    throw new FantooException(ErrorCode.ERROR_FE5012);
                }
            }
        }

        return fanitMessageResponse;
    }

    @Transactional
    public FanitMessageResponse updateFanitVoteAmofe(Account account, String integUid, String modelId) {
        FanitMessageResponse fanitMessageResponse = new FanitMessageResponse();
        this.uidCheck(account.getIntegUid(),integUid);
        Boolean isFanitPointUse = fanitMapper.isFanitPointUse(integUid,amofeAblePoint);

        if(!isFanitPointUse){
            throw new FantooException(ErrorCode.ERROR_FE5004,String.valueOf(fanitMapper.getFanitUseAblePoint(integUid,false)));
        }else {
            //int todayAblePoint = fanitMapper.getFanitUseAblePoint(integUid,true);
            //하루동안 사용할수 있는 1,000,000만원과 상관이 없다.
            int todayAmofeAblePointCnt = fanitMapper.getTodayPointCnt(integUid, modelId);
            //49번까지 가능
            if (todayAmofeAblePointCnt >= amofeAbleCnt) {
                throw new FantooException(ErrorCode.ERROR_FE5007);
            } else {
                String userIp = account.getUserIp();
                String ip = this.extractIpFromRequest(userIp);
                CountryData countryData = countryMapper.getIpNation(ip);
                String country = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountry();
                String countryCode = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountryCode();
                fanitMapper.insertFanitPoint(FanitPointInsertReq.builder()
                        .type("0")
                        .country(country)
                        .countryCode(countryCode)
                        .code(modelId)
                        .integUid(integUid)
                        .point(-1 * amofeAblePoint)
                        .userIp(userIp)
                        .build());
                fanitMessageResponse.setPoint(fanitMapper.getFanitUseAblePoint(integUid,false));
                fanitMessageResponse.setUsepoint(amofeAblePoint);
            }
        }
        return fanitMessageResponse;
    }

    @Transactional(readOnly = true)
    public String getFanitVote(int idx) {

        FanitList fanitDetail = fanitMapper.getFanitList(idx);
        String result = "";
        //유효하지 않은 idx일경우
        if (ObjectUtils.isEmpty(fanitDetail)) {
            throw new FantooException(ErrorCode.ERROR_FE5001);
        }
        //투표기간이 맞는지 확인
        Boolean isFanitPro = fanitMapper.isFanitPro(idx);
        if (!isFanitPro) {
            throw new FantooException(ErrorCode.ERROR_FE5003);
        }
        result = me.getLocaleMsg("FE5002",Locale.KOREA);
        return result;
    }

    @Transactional
    public FanitMessageResponse saveFanitVote(Account account, String integUid) {
        this.uidCheck(account.getIntegUid(),integUid);
        if(fanitMapper.isTodayLoginReward(integUid)){
            throw new FantooException(ErrorCode.ERROR_FE5008);
        }else{
            String userIp = account.getUserIp();
            String ip = this.extractIpFromRequest(userIp);
            CountryData countryData = countryMapper.getIpNation(ip);
            String country = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountry();
            String countryCode = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountryCode();
            fanitMapper.insertFanitPoint(FanitPointInsertReq.builder()
                    .type("1")
                    .country(country)
                    .countryCode(countryCode)
                    .integUid(integUid)
                    .point(saveDayFanitPoint)
                    .userIp(userIp)
                    .rewardType("LOGIN_REWARD")
                    .build());
        }
        return FanitMessageResponse.builder()
                .point(fanitMapper.getFanitUseAblePoint(integUid,false))
                .usepoint(saveDayFanitPoint)
                .build();
    }

    @Transactional
    public FanitMessageResponse amofeSavePoint(Account account, String integUid) {
        this.uidCheck(account.getIntegUid(),integUid);
        Boolean isTodayAmofeReward = fanitMapper.isTodayAmofeReward(integUid);
        if(isTodayAmofeReward){
            throw new FantooException(ErrorCode.ERROR_FE5009);
        }else{
            String userIp = account.getUserIp();
            String ip = this.extractIpFromRequest(userIp);
            CountryData countryData = countryMapper.getIpNation(ip);
            String country = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountry();
            String countryCode = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountryCode();
            FanitData fanitData = FanitData.builder()
                    .rewardType(RewardType.AMOFE2023_REWARD)
                    .integUid(integUid)
                    .type("1")
                    .userIp(userIp)
                    .country(country)
                    .countryCode(countryCode)
                    .build();
            FanitPointInsertReq fanitPointInsertReq = new FanitPointInsertReq(RewardType.AMOFE2023_REWARD,fanitData);
            Boolean isFanitPayment = fanitMapper.isRewardFanitPayment(fanitPointInsertReq);
            if(!isFanitPayment){
                fanitMapper.insertFanitPoint(fanitPointInsertReq);
            }else{
                throw new FantooException(ErrorCode.ERROR_FE5010);
            }
        }
        return FanitMessageResponse.builder()
                .point(fanitMapper.getFanitUseAblePoint(integUid,false))
                .usepoint(saveDayAmofePoint)
                .build();
    }

    @Transactional(readOnly = true)
    public FanitMessageResponse getMyPoint(Account account, String integUid) {
        this.uidCheck(account.getIntegUid(),integUid);
        int ablePoint = 0;
        ablePoint = fanitMapper.getFanitUseAblePoint(integUid,false);
        return FanitMessageResponse.builder()
                .point(ablePoint)
                .build();
    }

    @Transactional(readOnly = true)
    public FanitPointResponse getMyPointHistory(Account account, String integUid, String type) {
        List<FanitPoint> fanitPointList = new ArrayList<>();
        this.uidCheck(account.getIntegUid(), integUid);
        Boolean isIntegUidCheck = fanitMapper.isIntegUidCheck(integUid);
        if(isIntegUidCheck){
            fanitPointList = fanitMapper.getMyPointHistory(integUid,type);
        }else{
            throw new FantooException(ErrorCode.ERROR_FE5011);
        }
        return FanitPointResponse.builder()
                .fanitPointList(fanitPointList)
                .build();
    }

}
