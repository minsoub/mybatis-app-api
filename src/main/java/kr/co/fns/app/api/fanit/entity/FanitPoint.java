package kr.co.fns.app.api.fanit.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.api.country.entity.CountryData;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Fanit Point 정보")
public class FanitPoint extends CountryData {

    @Schema(description = "인덱스")
    private int idx;

    @Schema(description = "포인트 사용 적립 0 = 소비 1 = 적립")
    private String type;

    @Schema(description = "팬아이트 ID")
    private int fanitId;

    @Schema(description = "팬아이트 디테일 ID")
    private int fanitDetailId;

    @Schema(description = "회원 UID")
    private String integUid;

    @Schema(description = "포인트")
    private int point;

    @Schema(description = "사용자 IP")
    private String userIp;

    @Schema(description = "생성 일자", example = "2021-09-03 17:20:36")
    private String createDate;

    @Schema(description = "생성 날짜", example = "2021-09-03")
    private String createDay;

    @Schema(description = "관리자 여부")
    private int admin;

    @Schema(description = "보상 타입")
    private String rewardType;

    @Schema(description = "가입 날짜")
    private String joinDate;

    @Schema(description = "코드")
    private String code;

}