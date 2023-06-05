package kr.co.fns.app.api.fanit.mapper;

import kr.co.fns.app.api.fanit.entity.FanitList;
import kr.co.fns.app.api.fanit.entity.FanitListDetail;
import kr.co.fns.app.api.fanit.entity.FanitPoint;
import kr.co.fns.app.api.fanit.model.request.FanitPointInsertReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FanitMapper {

    FanitList getFanitList(int idx);

    List<FanitList> getFanitPro(int pageSize, int startRow);

    FanitList getDetail(int idx);

    List<FanitListDetail> getListDetail(int idx, int totalVoteCnt);

    FanitList getModifyFanitList(int idx);

    List<FanitListDetail> getFanitDetails(int idx, int totalVoteCnt);

    void modifyFanitListDetail(int idx, int totalVoteCnt);

    List<FanitList> getUpdateFanitList();

    Boolean isFanitPro(int idx);

    Boolean isFanitPointUse(String integUid, int point);

    int getFanitUseAblePoint(String integUid, boolean todayCheck);

    int insertFanitPoint(FanitPointInsertReq fanitPointInsertReq);

    int getTodayPointCnt(String integUid, String modelId);

    Boolean isTodayLoginReward(String integUid);

    Boolean isTodayAmofeReward(String integUid);

    List<FanitPoint> getMyPointHistory(String integUid, String type);

    Boolean isIntegUidCheck(String integUid);

    Boolean isFanitListDetail(int idx);

    Boolean isTodayFanitPayment(FanitPointInsertReq fanitPointInsertReq);

    Boolean isTodayCommunityFanitPayment(FanitPointInsertReq fanitPointInsertReq);

    Boolean isRewardFanitPayment(FanitPointInsertReq fanitPointInsertReq);

    String getClubManage(FanitPointInsertReq fanitPointInsertReq);

    String getReferralUser(FanitPointInsertReq fanitPointInsertReq);

    int getJoinRewardCnt(FanitPointInsertReq fanitPointInsertReq);


}