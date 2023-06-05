package kr.co.fns.app.api.minute.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinuteReply {

    @Schema(description = "Minute Reply 번호", example = "6")
    private BigInteger idx;

    @Schema(description = "Minute Type", example = "minute")
    private String subType;

    @Schema(description = "Minute 번호", example = "67")
    private BigInteger subIdx;

    @Schema(description = "통합 ID", example = "ft_u_f96a7595c13a11ecb6386f262a44c38b_xxxxx")
    private String integUid;

    @Schema(description = "사용자 ID", example = "ok_hjm@kakao.com")
    private String userId;

    @Schema(description ="Comment", example = "무서울수도..")
    private String comment;

    @Schema(description = "is_view", example = "1")
    private int isView;

    @Schema(description = "anonymous", example = "0")
    private int anonymous;

    @Schema(description = "Good Count", example = "138")
    private int goodCount;

    @Schema(description = "Bad Count", example = "138")
    private int badCount;

    @Schema(description = "생성 일자", example = "2023-02-14 23:48:14")
    private LocalDateTime createdDate;

    @Schema(description = "User Nickname", example = "한류타임스")
    private String userNickname;

    @Schema(description = "User Photo", example = "073b9b7-3896-42ec-9a2e-e6d3791d9b00")
    private String userPhoto;

    @Schema(description = "추천 비추천 타입", example = "good or bad")
    private String linkType;

    @Schema(description = "사용자 블럭 여부", example = "false")
    private boolean isUserBlock;

    @Schema(description = "Piece Block 여부", example = "false")
    private boolean isPieceBlock;

}
