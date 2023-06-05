package kr.co.fns.app.api.minute.service;


import kr.co.fns.app.api.minute.entity.MinuteDetail;
import kr.co.fns.app.api.minute.entity.MinuteHashtag;
import kr.co.fns.app.api.minute.entity.MinuteList;
import kr.co.fns.app.api.minute.mapper.MinuteMapper;
import kr.co.fns.app.api.minute.mapper.MinuteSoundTrackMapper;
import kr.co.fns.app.api.minute.model.request.MinuteInsertRequest;
import kr.co.fns.app.api.minute.model.request.MinuteUpdateRequest;
import kr.co.fns.app.api.minute.model.response.MinuteDetailResponse;
import kr.co.fns.app.api.minute.model.response.MinuteLastResponse;
import kr.co.fns.app.api.minute.model.response.MinuteListResponse;
import kr.co.fns.app.api.minute.model.response.MinuteSpecificListResponse;
import kr.co.fns.app.config.resolver.Account;
import kr.co.fns.app.core.aop.exception.ErrorCode;
import kr.co.fns.app.core.aop.exception.custom.FantooException;
import kr.co.fns.app.core.receiver.FanitData;
import kr.co.fns.app.core.receiver.enums.RewardType;
import kr.co.fns.app.core.sender.OciQueueSenderEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MinuteService {

    private final MinuteMapper minuteMapper;
    private final MinuteSoundTrackMapper minuteSoundTrackMapper;
    private final int showMinute = 15;
    private final ApplicationEventPublisher eventPublisher;


    /**
     * Minute 1개의 데이터를 리턴한다.
     *
     * @param idx
     * @param integUid
     * @return
     */
    @Transactional(readOnly = true)
    public MinuteDetailResponse getMinuteDetail(BigInteger idx, String integUid) {

        // 관리자 여부 조회 - token 없이 호출이 가능하므로
        boolean isAdmin = false;
        if (!StringUtils.isEmpty(integUid)) {
            int adminChkCount = minuteMapper.getContentAdminList(integUid);
            isAdmin = adminChkCount == 0 ? false : true;
        }

        // 미닛 상세 데이터 조회
        MinuteDetail minuteDetail = minuteMapper.getDetail(idx, integUid);

        return MinuteDetailResponse.builder()
                .admin(isAdmin)
                .minuteDetail(minuteDetail)
                .build();
    }

    /**
     * 미닛 마지막 작성자를 리턴한다.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public MinuteLastResponse getLastMinute() {
        String integUid = minuteMapper.getLastMinute();

        return MinuteLastResponse.builder()
                .integUid(integUid)
                .build();
    }

    @Transactional(readOnly = true)
    public MinuteLastResponse getMinuteReplyLast(BigInteger idx) {
        String integUid = minuteMapper.getMinuteReplyLast(idx);

        return MinuteLastResponse.builder()
                .integUid(integUid)
                .build();
    }

    @Transactional
    public int setMinuteViewCountUpdate(BigInteger idx) {
        int cnt = minuteMapper.updateViewCount(idx);

        return cnt;
    }

    /**
     * Minute List 조회
     *
     * @param integUid
     * @param page
     * @param nextCheck
     * @param sortingNum
     * @param  searchText
     * @return
     */
    @Transactional(readOnly = true)
    public MinuteListResponse getList(String integUid, int page, boolean nextCheck, int sortingNum, String searchText) {
        // total
        int startPos = 0;
        if (page > 1) {
            startPos = (page - 1) * showMinute;
        }
        int total = minuteMapper.getListMinuteTotal(integUid, page, nextCheck, sortingNum, showMinute, searchText);
        int totalPage = (int) Math.ceil(total);

        // list - int showMinute
        int lastLimit = page * showMinute;
        List<MinuteList> minutelList = minuteMapper.getListMinute(integUid, page, nextCheck, sortingNum, startPos, lastLimit, showMinute, searchText);

        // 관리자 여부
        int adminChkCount = minuteMapper.getContentAdminList(integUid);
        boolean isAdmin = adminChkCount == 0 ? false : true;

        return MinuteListResponse.builder()
                .totalPage(totalPage)
                .admin(isAdmin)
                .minuteList(minutelList)
                .build();
    }

    /**
     * 미닛 특정 지정된 리스트 조회
     *
     * @param targetUid
     * @param account
     * @param page
     * @param nextCheck
     * @param sortingNum
     * @param searchText
     * @param classifiValue
     * @return
     */
    @Transactional(readOnly = true)
    public MinuteSpecificListResponse getSpecificList(String targetUid, Account account, int page, boolean nextCheck, int sortingNum, String searchText, String classifiValue) {
        // total
        int startPos = 0;
        if (page > 1) {
            startPos = (page - 1) * showMinute;
        }
        int total = minuteMapper.getListMinuteSpecificTotal(targetUid, account.getIntegUid(), page, nextCheck, sortingNum, showMinute, searchText, classifiValue);
        int totalPage = (int) Math.ceil(total);

        // list - int showMinute
        int lastLimit = page * showMinute;
        List<MinuteList> minutelList = minuteMapper.getListMinuteSpecific(targetUid, account.getIntegUid(), page, nextCheck, sortingNum, startPos, lastLimit, showMinute, searchText, classifiValue);

        // 관리자 여부
        // TODO: getBlockList 검토
        int adminChkCount = minuteMapper.getBlockList(account.getIntegUid(), targetUid);
        boolean userBlockYn = adminChkCount == 0 ? false : true;

        return MinuteSpecificListResponse.builder()
                .totalPage(totalPage)
                .userBlockYn(userBlockYn)
                .minuteList(minutelList)
                .build();
    }

    /**
     * 미닛 신규 등록
     * @param account
     * @param minuteRequest
     * @return
     */
    @Transactional(readOnly = false)
    public String insertMinute(Account account, MinuteInsertRequest minuteRequest) {
        // Black List check
        int cntBlackList = minuteMapper.getBlackList(account.getIntegUid());
        if (cntBlackList >= 1) {
            throw new FantooException(ErrorCode.ERROR_FE4013); // 차단중
        }

        // Maximum write check
        int cntWriteLimit = minuteMapper.getMinuteLimit(account.getIntegUid());
        if (cntWriteLimit >= 10000) {
            throw new FantooException(ErrorCode.ERROR_FE4011);  // 하루 5회 쓰기 초과 (사용하지 않는 것 같음)
        }

        // 중복 제목 글 등록 체크
        int cntDupWrite = minuteMapper.getMinuteTitleCheck(account.getIntegUid(), minuteRequest.getTitle());
        if (cntDupWrite > 0) {
            throw new FantooException(ErrorCode.ERROR_FE4012);  // 중복젝목 사용 불가.
        }

        // 등록
        // validation
        if (StringUtils.isEmpty(minuteRequest.getUserNickname())) minuteRequest.setUserNickname("");
        if (StringUtils.isEmpty(minuteRequest.getUserPhoto())) minuteRequest.setUserPhoto("");
        if (StringUtils.isEmpty(minuteRequest.getImage())) minuteRequest.setImage("");
        if (StringUtils.isEmpty(minuteRequest.getVideo())) minuteRequest.setVideo("");

        int minuteIdx = minuteMapper.insertMinute(minuteRequest);

        // 해시태그 등록
        // 해시태그가 5개 이상인 경우
        if (minuteRequest.getHashtagList() != null) {
            if (minuteRequest.getHashtagList().size() > 5) {
                throw new FantooException(ErrorCode.ERROR_FE4016);
            }
            if (minuteRequest.getHashtagList().size() > 0) {
                List<MinuteHashtag> minuteHashtags = new ArrayList<MinuteHashtag>();
                for (String tag : minuteRequest.getHashtagList()) {
                    minuteHashtags.add(
                            MinuteHashtag.builder()
                                    .minuteId(minuteRequest.getIdx())
                                    .tag(tag)
                                    .build()
                    );
                }
                int result = minuteMapper.insertMinuteHashTag(minuteHashtags);
                if (result <= 0) {
                    throw new FantooException(ErrorCode.ERROR_FE4014);
                }
            }
        }

        // 음원 맵핑 등록
        if (minuteRequest.getMinuteSongId() != null) {
            log.debug("음원 등록 => {}", minuteRequest.getMinuteSongId());
            int songResult = minuteSoundTrackMapper.insertMappingTrack(minuteRequest.getIdx(), minuteRequest.getMinuteSongId());
            if (songResult == -1) {
                throw new FantooException(ErrorCode.ERROR_FE4015);
            }
        }
        // Fanit event 전송
        FanitData fanitData = FanitData.builder()
                .rewardType(RewardType.MINUTE_POST_REWARD)
                .integUid(account.getIntegUid())
                .refId(String.valueOf(minuteRequest.getIdx()))
                .type("1")
                .userIp(account.getUserIp())
                .country(minuteRequest.getLang())
                .countryCode(minuteRequest.getLang())
                .fanidId(null)
                .fainidDetailId(null)
                .description("")
                .build();
        OciQueueSenderEvent event = new OciQueueSenderEvent(this, fanitData);
        eventPublisher.publishEvent(event);

        return String.valueOf(minuteRequest.getIdx());
    }


    /**
     * 미닛 데이터를 수정한다.
     * @param integUid
     * @param idx
     * @param minuteRequest
     * @return
     */
    @Transactional(readOnly = false)
    public String updateMinute(String integUid, BigInteger idx, MinuteUpdateRequest minuteRequest) {
        // Black List check
        if (!integUid.equals(minuteRequest.getIntegUid())) {
            throw new FantooException(ErrorCode.ERROR_401);
        }

        // 수정
        // validation
        if (StringUtils.isEmpty(minuteRequest.getUserNickname())) minuteRequest.setUserNickname("");
        if (StringUtils.isEmpty(minuteRequest.getImage())) minuteRequest.setImage("");

        minuteRequest.setIdx(idx);

        int minuteIdx = minuteMapper.updateMinute(minuteRequest);
//
//        // 해시태그 등록
//        if (minuteRequest.getHashtagList().size() > 0) {
//            List<MinuteHashtag> minuteHashtags = new ArrayList<MinuteHashtag>();
//            for (String tag: minuteRequest.getHashtagList()) {
//                minuteHashtags.add(
//                        MinuteHashtag.builder()
//                                .minuteId(minuteIdx)
//                                .tag(tag)
//                                .build()
//                );
//            }
//            int result = minuteMapper.insertMinuteHashTag(minuteHashtags);
//            if (result <= 0) {
//                throw new FantooException(ErrorCode.ERROR_FE4014);
//            }
//        }

        return String.valueOf(minuteIdx);
    }

    /**
     * 미닛 게시글을 삭제한다.
     *
     * @param idx
     * @param integUid
     * @return
     */
    @Transactional(readOnly = false)
    public String deleteMinute(BigInteger idx, String integUid) {
        int adminChkCount = minuteMapper.getContentAdminList(integUid);
        boolean isAdmin = adminChkCount == 0 ? false : true;

        return String.valueOf(minuteMapper.deleteMinute(idx, integUid, isAdmin));
    }

    /**
     * 미닛 게시글 등록 가능한지 체크한다.
     * 블랙 리스트에 등록된 데이터는 등록된 일자와 term_day를 관리한다.
     * @param integUid
     * @return
     */
    @Transactional(readOnly = true)
    public String isMinuteWritable(String integUid) {

        int cntBlackList = minuteMapper.getBlackList(integUid);
        if (cntBlackList >= 1) {
            throw new FantooException(ErrorCode.ERROR_FE4013); // 차단중
        } else {
            return "OK";
        }
    }
}
