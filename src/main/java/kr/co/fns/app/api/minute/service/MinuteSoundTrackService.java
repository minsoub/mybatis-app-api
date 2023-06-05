package kr.co.fns.app.api.minute.service;

import kr.co.fns.app.api.minute.entity.MinuteList;
import kr.co.fns.app.api.minute.entity.MinuteSoundTrack;
import kr.co.fns.app.api.minute.mapper.MinuteMapper;
import kr.co.fns.app.api.minute.mapper.MinuteSoundTrackMapper;
import kr.co.fns.app.api.minute.model.response.MinuteListResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MinuteSoundTrackService {

    private final MinuteSoundTrackMapper minuteSoundTrackMapper;
    private final MinuteMapper minuteMapper;

    private final int showMinute = 9;

    /**
     * 음원 상세 정보를 리턴한다.
     * active_status : 활성화여부(0:비활성,1:활성,2:삭제)
     * @param integUid
     * @param minuteSongId
     * @return
     */
    public MinuteSoundTrack soundTrackDetail(String integUid, BigInteger minuteSongId)  {
        int activeStatus = 1; // 활성
        MinuteSoundTrack minuteSoundTrack = minuteSoundTrackMapper.selectSoundTrackDetail(integUid, minuteSongId, activeStatus);

        return minuteSoundTrack;
    }

    /**
     * 음원과 맵핑된 미닛 리스트를 조회한다.
     *
     * @param integUid (옵션)
     * @param minuteSongId
     * @param page
     * @param nextCheck
     */
    public MinuteListResponse minuteSoundTrackList(String integUid, BigInteger minuteSongId, int page, boolean nextCheck) {

        // total
        int startPos = 0;
        if (page > 1) {
            startPos = (page - 1) * showMinute;
        }
        int total = minuteMapper.getListMinuteSoundTrackTotal(integUid, page, nextCheck, showMinute, minuteSongId);
        int totalPage = (int) Math.ceil(total);

        // list - int showMinute
        int lastLimit = page * showMinute;
        List<MinuteList> minutelList = minuteMapper.getListMinuteSoundTrack(integUid, page, nextCheck, startPos, lastLimit, showMinute, minuteSongId);

        // 관리자 여부
        int adminChkCount = minuteMapper.getContentAdminList(integUid);
        boolean isAdmin = adminChkCount == 0 ? false : true;

        return MinuteListResponse.builder()
                .totalPage(totalPage)
                .admin(isAdmin)
                .minuteList(minutelList)
                .build();
    }
}
