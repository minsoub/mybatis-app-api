package kr.co.fns.app.api.fanit.entity;

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
@Schema(description = "Fanit 스토리 데이터")
public class FanitStory extends CountryData {

    @Schema(description = "댓글 번호", example = "99")
    private Integer idx;

    @Schema(description = "댓글단 팬잇 번호")
    private Integer fanitId;

    @Schema(description = "회원 UID")
    private String integUid;

    @Schema(description = "User UID")
    private String userUid;

    @Schema(description = "User ID")
    private String userId;

    @Schema(description = "Comment")
    private String comment;

    @Schema(description = "유저 IP")
    private String userIp;

    @Schema(description = "Create Date", example = "2023-04-22 01:26:16")
    private String createDate;

    @Schema(description = "노출여부 YN")
    private Integer svcYn;

    @Schema(description = "User Nickname", example = "유저 닉네임")
    private String userNickname;

    @Schema(description = "User Photo", example = "abb6af5a-25c1-4951-4278-d0ec6ad36a00")
    private String userPhoto;

}
