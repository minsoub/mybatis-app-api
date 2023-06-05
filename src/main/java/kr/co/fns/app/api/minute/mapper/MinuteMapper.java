package kr.co.fns.app.api.minute.mapper;

import kr.co.fns.app.api.minute.entity.*;
import kr.co.fns.app.api.minute.model.enums.LikeType;
import kr.co.fns.app.api.minute.model.request.MinuteInsertRequest;
import kr.co.fns.app.api.minute.model.request.MinuteUpdateRequest;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface MinuteMapper {
    Minute getMinute(Integer targetId);
    MinuteReply getMinuteReply(Integer targetId);

    /**
     * 콘텐츠 관리자인지 체크한다.
     *
     * @param integUid
     * @return
     */
    int getContentAdminList(String integUid);

    /**
     * 미닛 상세 데이터를 리턴한다.
     *
     * @param idx
     * @param integUid (값이 없을 수 있다)
     * @return
     */
    MinuteDetail getDetail(BigInteger idx, String integUid);

    /**
     * 미닛 마지막 작성자를 리턴한다.
     *
     * @return
     */
    String getLastMinute();

    String getMinuteReplyLast(BigInteger idx);

    /**
     * 조회 수 증가
     * @param idx
     * @return
     */
    int updateViewCount(BigInteger idx);

    /**
     * 댓글 카운트 증가
     * @param idx Minute idx
     * @return
     */
    int updateReplyCount(BigInteger idx);

    /**
     * 해당 Minute의 Reply count를 설정한다.
     * @param idx
     * @param replyCount
     * @return
     */
    int updateReplyCountSync(BigInteger idx, int replyCount);

    /**
     * 댓글 카운트 감소
     * @param idx
     * @return
     */
    int updateReplyMinus(BigInteger idx);

    /**
     * 미닛 리스트 총 개수 조회
     *
     * @param integUid
     * @param page
     * @param nextCheck
     * @param sortingNum
     * @param showMinute
     * @param searchText
     * @return
     */
    int getListMinuteTotal(String integUid, int page, boolean nextCheck, int sortingNum, int showMinute, String searchText);

    /**
     * 미닛 리스트 조회
     *
     * @param integUid
     * @param page
     * @param nextCheck
     * @param sortingNum
     * @param startPos
     * @param lastLimit
     * @param showMinute
     * @param searchText
     * @return
     */
    List<MinuteList> getListMinute(String integUid, int page, boolean nextCheck, int sortingNum, int startPos, int lastLimit, int showMinute, String searchText);


    int getListMinuteSpecificTotal(String targetUid, String integUid, int page, boolean nextCheck, int sortingNum, int showMinute, String searchText, String classifiValue);

    List<MinuteList> getListMinuteSpecific(String targetUid, String integUid, int page, boolean nextCheck, int sortingNum, int startPos, int lastLimit, int showMinute, String searchText, String classifiValue);

    /**
     * 미닛 리스트 총 개수 조회
     *
     * @param integUid
     * @param page
     * @param nextCheck
     * @param showMinute
     * @param minuteSongId
     * @return
     */
    int getListMinuteSoundTrackTotal(String integUid, int page, boolean nextCheck, int showMinute, BigInteger minuteSongId);

    /**
     * 미닛 리스트 조회
     *
     * @param integUid
     * @param page
     * @param nextCheck
     * @param startPos
     * @param lastLimit
     * @param showMinute
     * @param minuteSongId
     * @return
     */
    List<MinuteList> getListMinuteSoundTrack(String integUid, int page, boolean nextCheck, int startPos, int lastLimit, int showMinute, BigInteger minuteSongId);

    /**
     * Minute 신규 등록
     * @param minuteInsertRequest
     * @return
     */
    int insertMinute(MinuteInsertRequest minuteInsertRequest);

    /**
     * 미닛 정보 수정
     * @param minuteUpdateRequest
     * @return
     */
    int updateMinute(MinuteUpdateRequest minuteUpdateRequest);

    /**
     * 미닛 정보 삭제
     *
     * @param idx
     * @param integUid
     * @param isAdmin
     * @return
     */
    int deleteMinute(BigInteger idx, String integUid, boolean isAdmin);

    /**
     * Minute 해시태그 등록
     *
     * @param hashtagList
     * @return
     */
    int insertMinuteHashTag(List<MinuteHashtag> hashtagList);

    /**
     * Block 회원인지 체크
     * @param integUid
     * @param targetUid
     * @return
     */
    int getBlockList(String integUid, String targetUid);

    /**
     * Black List check
     *
     * @param integUid
     * @return
     */
    int getBlackList(String integUid);

    /**
     * Minute Limit check (1day)
     * @param integUid
     * @return
     */
    int getMinuteLimit(String integUid);

    /**
     * Minute Title Duplication check (1day)
     * @param integUid
     * @param title
     * @return
     */
    int getMinuteTitleCheck(String integUid, String title);

    /**
     * Minute good_count, bad_count 값을 설정한다.
     * @param idx
     * @param likeType (good, good_cancel, bad, bad_cancel)
     * @return
     */
    int updateLikeCount(BigInteger idx, String likeType);

    /**
     * Minute count 관련 항목을 조회한다.
     * @param idx
     * @return
     */
    MinuteResult selectCountType(BigInteger idx);
}
