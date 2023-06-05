package kr.co.fns.app.api.fanit.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.core.receiver.FanitData;
import kr.co.fns.app.core.receiver.enums.RewardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "FanitPoint 신규 등록 데이터")
public class FanitPointInsertReq {

    @Schema(description = "Fanit 타입", example = "0은 사용 or 1은 지급")
    private String type;

    @Schema(description = "팬잇 ID")
    private int fanitId;

    @Schema(description = "팬잇 디테일 ID")
    private int fanitDetailId;

    @Schema(description = "코드")
    private String code;

    @Schema(description = "회원 UID")
    private String integUid;

    @Schema(description = "포인트")
    private int point;

    @Schema(description = "사용자 IP")
    private String userIp;

    @Schema(description = "보상 타입")
    private String rewardType;

    @Schema(description = "하루 적립가능횟수")
    private int possibleDayCnt;

    @Schema(description = "적립가능횟수")
    private int possibleCnt;

    @Schema(description = "국가")
    private String country;

    @Schema(description = "국가 코드")
    private String countryCode;

    @Schema(description = "클럽 가입자수")
    private String description;

    public FanitPointInsertReq(RewardType rewardType, FanitData fanitData) {
        this.integUid = fanitData.getIntegUid();
        this.type = fanitData.getType();
        this.code = fanitData.getRefId();
        this.point = rewardType.getPoint();
        this.userIp = fanitData.getUserIp();
        this.country = fanitData.getCountry();
        this.countryCode = fanitData.getCountryCode();
        this.rewardType = String.valueOf(fanitData.getRewardType());
        this.possibleDayCnt = rewardType.getPossibleDayCnt();
        this.possibleCnt = rewardType.getPossibleCnt();
        this.description = fanitData.getDescription();
    }

}
