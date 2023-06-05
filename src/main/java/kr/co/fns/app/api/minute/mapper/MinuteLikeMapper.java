package kr.co.fns.app.api.minute.mapper;

import kr.co.fns.app.api.minute.model.enums.LikeType;
import kr.co.fns.app.api.minute.model.enums.SubType;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;

@Mapper
public interface MinuteLikeMapper {

    /**
     * 현재 상태의 Like Type을 리턴한다.
     *
     * @param idx
     * @param subType
     * @param integUid
     * @return
     */
    String getCurrentMinuteLikeType(BigInteger idx, SubType subType, String integUid);

    /**
     * Minute Like를 삭제한다.
     *
     * @param subIdx
     * @param subType
     * @param integUid
     * @return
     */
    int deleteMinuteLike(BigInteger subIdx, SubType subType, String integUid);

    /**
     * Minute like를 신규 등록한다.
     * @param subIdx
     * @param subType
     * @param integUid
     * @param likeType
     * @return
     */
    int insertMinuteLike(BigInteger subIdx, SubType subType, String integUid, LikeType likeType);
}
