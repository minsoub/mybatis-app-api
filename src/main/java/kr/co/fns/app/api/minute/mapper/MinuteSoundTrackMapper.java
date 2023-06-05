package kr.co.fns.app.api.minute.mapper;

import kr.co.fns.app.api.minute.entity.MinuteSoundTrack;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;

@Mapper
public interface MinuteSoundTrackMapper {
    /**
     * 음원 상세 정보를 리턴한다.
     * @param integUid
     * @param minuteSongId
     * @param activeStatus
     * @return
     */
    MinuteSoundTrack selectSoundTrackDetail(String integUid, BigInteger minuteSongId, int activeStatus);

    /**
     * 미닛과 음원 맵핑 등록
     *
     * @param minuteId
     * @param minuteSongId
     * @return
     */
    int insertMappingTrack(BigInteger minuteId, BigInteger minuteSongId);
}
