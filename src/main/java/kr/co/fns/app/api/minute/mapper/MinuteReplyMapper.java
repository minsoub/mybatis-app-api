package kr.co.fns.app.api.minute.mapper;

import kr.co.fns.app.api.minute.entity.MinuteList;
import kr.co.fns.app.api.minute.entity.MinuteReply;
import kr.co.fns.app.api.minute.entity.MinuteResult;
import kr.co.fns.app.api.minute.model.enums.LikeType;
import kr.co.fns.app.api.minute.model.request.MinuteReplyInsertRequest;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface MinuteReplyMapper {

    /**
     * Minute Reply 총 글 수를 리턴한다.
     * @param idx
     * @param integUid
     * @param page
     * @param nextCheck
     * @param topIdx
     * @param order
     * @param subType
     * @param showReply
     * @return
     */
    int getMinuteReplyListTotal(BigInteger idx, String integUid, int page, boolean nextCheck, int topIdx, String order, String subType, int showReply);

    /**
     * Minute Reply 리스트를 리턴한다.
     * @param idx
     * @param integUid
     * @param page
     * @param nextCheck
     * @param topIdx
     * @param order
     * @param startPos
     * @param lastLimit
     * @param subType
     * @param showReply
     * @return
     */
    List<MinuteReply> getMinuteReplyList(BigInteger idx, String integUid, int page, boolean nextCheck, int topIdx, String order,
                                         int startPos, int lastLimit, String subType, int showReply);

    /**
     * 미닛 댓글 신규 등록
     * @param minuteReplyInsertRequest
     * @return
     */
    int insertMinuteReply(MinuteReplyInsertRequest minuteReplyInsertRequest);


    /**
     * 미닛 댓글 View 설정
     * @param idx
     * @param subIdx
     * @param integUid
     * @param isView
     * @return
     */
    int updateMinuteReplyView(BigInteger idx, BigInteger subIdx, String integUid, int isView);


    /**
     * Minute Reply의 good_count, bad_count 값을 설정한다.
     * @param idx
     * @param likeType (good, good_cancel, bad, bad_cancel)
     * @return
     */
    int updateLikeCount(BigInteger idx, String likeType);

    /**
     * Minute Reply count 관련 항목을 조회한다.
     * @param idx
     * @return
     */
    MinuteResult selectCountType(BigInteger idx);

    /**
     * 해당 Minute의 Reply count를 리턴한다.
     *
     * @param idx
     * @return
     */
    int selectReplyCount(BigInteger idx);
}
