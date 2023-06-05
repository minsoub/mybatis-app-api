package kr.co.fns.app.api.minute.service;

import kr.co.fns.app.api.minute.entity.MinuteLinkType;
import kr.co.fns.app.api.minute.entity.MinuteResult;
import kr.co.fns.app.api.minute.mapper.MinuteFavoritesMapper;
import kr.co.fns.app.api.minute.mapper.MinuteLikeMapper;
import kr.co.fns.app.api.minute.mapper.MinuteMapper;
import kr.co.fns.app.api.minute.mapper.MinuteReplyMapper;
import kr.co.fns.app.api.minute.model.enums.LikeType;
import kr.co.fns.app.api.minute.model.enums.SubType;
import kr.co.fns.app.api.minute.model.response.MinuteLikeResponse;
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

import java.math.BigInteger;
import java.util.Locale;

@Slf4j
@Service
@AllArgsConstructor
public class MinuteLikeService {
    private final MinuteLikeMapper minuteLikeMapper;
    private final MinuteMapper minuteMapper;
    private final MinuteReplyMapper minuteReplyMapper;
    private final MinuteFavoritesMapper minuteFavoritesMapper;
    private final ApplicationEventPublisher eventPublisher;
    /**
     * 미닛 Like를 설정한다.
     *
     * @param account
     * @param idx
     * @param subType ('minute')
     * @param likeType ('good', 'bad', 'good_cancel', 'bdd_cancel')
     * @return
     */
    public MinuteLikeResponse setMinuteLike(Locale locale, Account account, BigInteger idx, SubType subType, LikeType likeType) {

        String resultLikeType = minuteLikeMapper.getCurrentMinuteLikeType(idx, subType, account.getIntegUid());

        // like type check
        if ( (likeType == LikeType.good || likeType == LikeType.bad) && !StringUtils.isEmpty(resultLikeType)) {
            log.debug("likeType => {}, {} ", likeType, resultLikeType);
            throw new FantooException(ErrorCode.ERROR_FE4006); // like type error
        } else if(likeType == LikeType.good_cancel || likeType == LikeType.bad_cancel) {
            if (StringUtils.isEmpty(resultLikeType)) {
                throw new FantooException(ErrorCode.ERROR_FE4006);
            } else if (resultLikeType.equals("good") && likeType.equals("bad_cancel")) {
                throw new FantooException(ErrorCode.ERROR_FE4006);
            } else if (resultLikeType.equals("bad") && likeType.equals("good_cancel")) {
                throw new FantooException(ErrorCode.ERROR_FE4006);
            }
        }

        // likeType is 'good_cancel' or 'bad_cancel' then delete like
        int result = 0;
        if (likeType == LikeType.good_cancel || likeType == LikeType.bad_cancel) {
            result = minuteLikeMapper.deleteMinuteLike(idx, subType, account.getIntegUid());
        } else {
            result = minuteLikeMapper.insertMinuteLike(idx, subType, account.getIntegUid(), likeType);
        }

        if (result > 0) {
            MinuteResult minuteResult = null;
            if (subType == SubType.minute) {
                minuteMapper.updateLikeCount(idx, String.valueOf(likeType));

                minuteResult = minuteMapper.selectCountType(idx);

                if (likeType == LikeType.good) {
                    // Fanit event 전송
                    FanitData fanitData = FanitData.builder()
                            .rewardType(RewardType.MINUTE_LIKE_REWARD)
                            .integUid(account.getIntegUid())
                            .refId(String.valueOf(idx))
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
                }
            } else if(subType == SubType.reply || subType == SubType.inReply) {
                minuteReplyMapper.updateLikeCount(idx, String.valueOf(likeType));
                minuteResult = minuteReplyMapper.selectCountType(idx);
            }
            // minute favorites 존재 여부
            int favType = minuteFavoritesMapper.isMinuteFavorites(idx, account.getIntegUid());

            String strLikeType = minuteLikeMapper.getCurrentMinuteLikeType(idx, subType, account.getIntegUid());
            log.debug("strLikeType => {}", strLikeType);

            return MinuteLikeResponse.builder()
                        .returnResult(minuteResult)
                        .countResult(MinuteLinkType.builder()
                                .likeType(StringUtils.isEmpty(strLikeType) ? "none" : strLikeType)
                                .favType(favType)
                                .build())
                        .build();
        } else {
            throw new FantooException(ErrorCode.ERROR_FE4006);
        }
    }

    /**
     * 좋아요 눌렸는지 체크한다.
     * 현재 LikeType을 리턴한다.
     *
     * @param integUid
     * @param idx
     * @param subType
     * @return
     */
    public String currentMinuteLike(String integUid, BigInteger idx, SubType subType) {
        String resultLikeType = minuteLikeMapper.getCurrentMinuteLikeType(idx, subType, integUid);

        return resultLikeType;
    }
}
