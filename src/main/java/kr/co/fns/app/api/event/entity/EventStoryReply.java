package kr.co.fns.app.api.event.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.api.country.entity.CountryData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "댓글 Event 데이터")
public class EventStoryReply extends CountryData {
    @Schema(description = "댓글 이벤트 번호", example = "99", readOnly = true)
    private Integer idx;

    @Schema(description = "대댓글단 댓글 인덱스 번호")
    private Integer storyId;

    @Schema(description = "통합 사용자 구분 integration uid (사용자를 구분할 수 있는, 고유 식별자)")
    private String integUid;

    @Schema(description = "유저 고유번호 UID")
    private String userUid;

    @Schema(description = "유저 ID")
    private String userId;

    @Schema(description = "댓글내용")
    private String comment;

    @Schema(description = "작성자 IP")
    private String userIp;

    @Schema(description = "생성 일자", example="2023-04-21 21:36:17")
    private String createDate;

    @Schema(description = "노출 여부", example = "노출여부, 0 : 비노출 , 1 : 노출 ")
    private Integer svcYn;

    @Schema(description = "유저 닉네임")
    private String userNickname;

    @Schema(description = "User Profile")
    private Integer userProfile;

    @Schema(description = "유저 가입일")
    private String joinDate;

    @Schema(description = "User Photo", example = "073b9b7-3896-42ec-9a2e-e6d3791d9b00")
    private String userPhoto;
}
