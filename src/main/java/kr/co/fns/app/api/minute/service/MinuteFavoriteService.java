package kr.co.fns.app.api.minute.service;

import kr.co.fns.app.api.minute.entity.*;
import kr.co.fns.app.api.minute.mapper.MinuteFavoritesMapper;
import kr.co.fns.app.api.minute.mapper.MinuteLikeMapper;
import kr.co.fns.app.api.minute.mapper.MinuteMapper;
import kr.co.fns.app.api.minute.mapper.MinuteReplyMapper;
import kr.co.fns.app.api.minute.model.enums.FavoriteType;
import kr.co.fns.app.api.minute.model.enums.SubType;
import kr.co.fns.app.api.minute.model.response.*;
import kr.co.fns.app.core.aop.exception.ErrorCode;
import kr.co.fns.app.core.aop.exception.custom.FantooException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MinuteFavoriteService {
    private final MinuteLikeMapper minuteLikeMapper;
    private final MinuteMapper minuteMapper;
    private final MinuteReplyMapper minuteReplyMapper;
    private final MinuteFavoritesMapper minuteFavoritesMapper;

    private final int showMinute = 15;

    /**
     * 미닛 즐겨찾기를 설정한다.
     *
     * @param integUid
     * @param idx
     * @param favType ('fav', 'fav_cancel')
     * @return
     */
    @Transactional(readOnly = false)
    public MinuteFavoriteResponse setMinuteFavorite(String integUid, BigInteger idx, FavoriteType favType) {

        // 기존 상태 체크
        int isFavorite = minuteFavoritesMapper.isMinuteFavorites(idx, integUid);

        if (favType == FavoriteType.fav && isFavorite > 0) {
            throw new FantooException(ErrorCode.ERROR_FE4007);  // 이미 등록된 즐겨찾기
        } else if (favType == FavoriteType.fav_cancel && isFavorite > 0) {
            throw new FantooException(ErrorCode.ERROR_FE4008);  // 삭제한 즐겨찾기
        }

        // favType is 'fav' or 'fav_cancel' then delete favorites
        int result = 0;
        if (favType == FavoriteType.fav_cancel) {
            result = minuteFavoritesMapper.deleteMinuteFavorite(idx, integUid);
        } else {
            result = minuteFavoritesMapper.insertMinuteFavorite(idx, integUid);
        }

        if (result > 0) {
            MinuteResult minuteResult = minuteMapper.selectCountType(idx);

            // minute favorites 존재 여부
            int intFavType = minuteFavoritesMapper.isMinuteFavorites(idx, integUid);

            String strLikeType = minuteLikeMapper.getCurrentMinuteLikeType(idx, SubType.minute, integUid);

            return MinuteFavoriteResponse.builder()
                    .returnResult(minuteResult)
                    .countResult(MinuteLinkType.builder()
                            .likeType(StringUtils.isEmpty(strLikeType) ? "none" : strLikeType)
                            .favType(intFavType)
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
     * @return
     */
    @Transactional(readOnly = true)
    public MinuteFavoriteDetailResponse currentMinuteFavorite(String integUid, BigInteger idx) {
        MinuteFavorites minuteFavorites = minuteFavoritesMapper.getMinuteFavorites(idx, integUid);

        return MinuteFavoriteDetailResponse.builder()
                .isFavorite(minuteFavorites == null ? false : true)
                .minuteFavorites(minuteFavorites)
                .build();
    }

    /**
     * 나의 즐겨찾기 리스트를 리턴한다.
     *
     * @param integUid
     * @param page
     * @param nextCheck
     * @param sortingNum (3 or 9)
     * @return
     */
    @Transactional(readOnly = true)
    public MinuteFavoritesListResponse getFavoritesList(String integUid, int page, boolean nextCheck, int sortingNum) {
        // total
        int startPos = 0;
        if (page > 1) {
            startPos = (page - 1) * showMinute;
        }
        int total = minuteFavoritesMapper.getMinuteFavoritesTotal(integUid, page, nextCheck, sortingNum, showMinute);
        int totalPage = (int) Math.ceil(total);

        // list - int showMinute
        int lastLimit = page * showMinute;
        List<MinuteFavoritesDetail> minutelList = minuteFavoritesMapper.getMinuteFavoritesList(integUid, page, nextCheck, sortingNum, startPos, lastLimit, showMinute);

        return MinuteFavoritesListResponse.builder()
                .totalPage(totalPage)
                .minuteList(minutelList)
                .build();
    }
}
