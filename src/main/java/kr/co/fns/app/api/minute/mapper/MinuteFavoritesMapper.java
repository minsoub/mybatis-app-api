package kr.co.fns.app.api.minute.mapper;

import kr.co.fns.app.api.minute.entity.MinuteFavorites;
import kr.co.fns.app.api.minute.entity.MinuteFavoritesDetail;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface MinuteFavoritesMapper {

    /**
     * Minute Favorites가 존재하는지 체크한다.
     *
     * @param subIdx
     * @param integUid
     * @return 0 or 1
     */
    int isMinuteFavorites(BigInteger subIdx, String integUid);

    /**
     * 등록된 즐겨 찾기를 삭제한다.
     * @param subIdx
     * @param integUid
     * @return
     */
    int deleteMinuteFavorite(BigInteger subIdx, String integUid);

    /**
     * 즐겨 찾기를 신규 등록한다.
     * @param subIdx
     * @param integUid
     * @return
     */
    int insertMinuteFavorite(BigInteger subIdx, String integUid);

    /**
     * 즐겨 찾기 상세정보를 리턴한다.
     * @param subIdx
     * @param integUid
     * @return
     */
    MinuteFavorites getMinuteFavorites(BigInteger subIdx, String integUid);

    /**
     * 나의 미닛 즐겨찾기 총 개수를 리턴한다.
     *
     * @param integUid
     * @param page
     * @param nextCheck
     * @param sortingNum
     * @param showMinute
     * @return
     */
    int getMinuteFavoritesTotal(String integUid, int page, boolean nextCheck, int sortingNum, int showMinute);

    /**
     * 나의 미닛 즐겨찾기 리스트를 리턴한다.
     * @param integUid
     * @param page
     * @param nextCheck
     * @param sortingNum
     * @param startPos
     * @param lastLimit
     * @param showMinute
     * @return
     */
    List<MinuteFavoritesDetail> getMinuteFavoritesList(String integUid, int page, boolean nextCheck, int sortingNum, int startPos, int lastLimit, int showMinute);

}
