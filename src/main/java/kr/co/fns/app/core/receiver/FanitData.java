package kr.co.fns.app.core.receiver;

import kr.co.fns.app.core.receiver.enums.RewardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class FanitData {
    private String integUid;
    private RewardType rewardType;
    private String type;  // 1 : 지급, 0 : 사용
    private String userIp;
    private String country;
    private String countryCode;
    private Integer fanidId;
    private Integer fainidDetailId;
    private String refId;  // 참조 ID (Club인 경우 Club ID)
    private String description;  // 50, 100  => 클럽 가입자수

    public FanitData(String integUid, String rewardType, String type, String userIp, String country, String countryCode, Integer fanidId, Integer fainidDetailId, String refId, String description)
    {
        this.integUid = integUid;
        this.rewardType = RewardType.valueOf(rewardType);
        this.type = type;
        this.userIp = userIp;
        this.country = country;
        this.countryCode = countryCode;
        this.fanidId = fanidId;
        this.fainidDetailId = fainidDetailId;
        this.refId = refId;
        this.description = description;
    }
}

