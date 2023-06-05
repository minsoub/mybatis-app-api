package kr.co.fns.app.api.minute.service;

import kr.co.fns.app.api.minute.entity.MinuteReply;
import kr.co.fns.app.api.minute.mapper.MinuteMapper;
import kr.co.fns.app.api.minute.mapper.MinuteReplyMapper;
import kr.co.fns.app.api.minute.model.request.MinuteReplyInsertRequest;
import kr.co.fns.app.api.minute.model.response.MinuteReplyListResponse;
import kr.co.fns.app.config.resolver.Account;
import kr.co.fns.app.core.receiver.FanitData;
import kr.co.fns.app.core.receiver.enums.RewardType;
import kr.co.fns.app.core.sender.OciQueueSenderEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@AllArgsConstructor
public class MinuteReplyService {

    private final MinuteReplyMapper minuteReplyMapper;
    private final MinuteMapper minuteMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final int showReply = 20;

    public MinuteReplyListResponse getMinuteReplyList(BigInteger idx, String integUid, int page, boolean nextCheck, int topIdx, String order, String subType) {

        // total
        int startPos = 0;
        if (page > 1) {
            startPos = (page - 1) * showReply;
        }
        int total = minuteReplyMapper.getMinuteReplyListTotal(idx, integUid, page, nextCheck, topIdx, order, subType, showReply);
        int totalPage = (int) Math.ceil(total/showReply);

        // list - int showMinute
        int lastLimit = page * showReply;
        List<MinuteReply> minutelList = minuteReplyMapper.getMinuteReplyList(idx, integUid, page, nextCheck, topIdx, order, startPos, lastLimit, subType, showReply);

        return MinuteReplyListResponse.builder()
                .totalPage(totalPage)
                .totalLength(total)
                .minuteReplyList(minutelList)
                .build();
    }

    /**
     * 미닛 댓글 등록
     * @param idx
     * @param account
     * @param minuteReplyRequest
     * @return
     */
    public String insertMinuteReply(Locale locale, BigInteger idx, Account account, MinuteReplyInsertRequest minuteReplyRequest) {
        if (StringUtils.isEmpty(minuteReplyRequest.getUserPhoto())) minuteReplyRequest.setUserPhoto("");
        if (StringUtils.isEmpty(minuteReplyRequest.getUserPhoto())) minuteReplyRequest.setUserPhoto("");
        if (StringUtils.isEmpty(minuteReplyRequest.getUserId())) minuteReplyRequest.setUserId("");
        if (StringUtils.isEmpty(minuteReplyRequest.getUserNickname())) minuteReplyRequest.setUserNickname("");

        minuteReplyRequest.setSubIdx(idx);

        int result = minuteReplyMapper.insertMinuteReply(minuteReplyRequest);

        if (result > 0) {
            // 미닛 댓글 count 증가
            minuteMapper.updateReplyCount(idx);
        }

        // Fanit event 전송
        FanitData fanitData = FanitData.builder()
                .rewardType(RewardType.MINUTE_REPLY_REWARD)
                .integUid(account.getIntegUid())
                .refId(String.valueOf(minuteReplyRequest.getIdx()))
                .type("1")
                .userIp(account.getUserIp())
                .country(locale.getDisplayName())
                .countryCode(locale.getCountry())
                .fanidId(null)
                .fainidDetailId(null)
                .description("")
                .build();

        OciQueueSenderEvent event = new OciQueueSenderEvent(this, fanitData);
        eventPublisher.publishEvent(event);

        return String.valueOf(result);
    }

    /**
     * 미닛 댓글 View 설정 - 삭제 포함.
     *
     * @param idx
     * @param replyIdx
     * @param integUid
     * @return
     */
    public String deleteMinuteReply(BigInteger idx, BigInteger replyIdx, String integUid) {
        int result = minuteReplyMapper.updateMinuteReplyView(replyIdx, idx, integUid, 0);

        if (result > 0) {
            minuteMapper.updateReplyMinus(idx);
        }
        return String.valueOf(result);
    }

    /**
     * 미닛 댓글 count를 설정한다.
     *
     * @param idx
     * @return
     */
    public String minuteReplySync(BigInteger idx) {
        int resultCount = minuteReplyMapper.selectReplyCount(idx);

        // minute의 reply_count set
        int result = minuteMapper.updateReplyCountSync(idx, resultCount);

        return String.valueOf(result);
    }
}
