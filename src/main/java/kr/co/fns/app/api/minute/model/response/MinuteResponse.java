package kr.co.fns.app.api.minute.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Minute 데이터 응답")
public class MinuteResponse {
    @Schema(description = "Minute 번호", example = "34")
    private Integer idx;

    @Schema(description = "통합 ID", example = "ft_u_f96a7595c13a11ecb6386f262a44c38b_xxxxx")
    private String integUid;

    @Schema(description = "사용자 UID", example = "63e3290xxxxx406520ecd0f0")
    private String userUid;

    @Schema(description = "언어코드", example = "kr")
    private String lang;

    @Schema(description = "제목", example = "#드림노트 #보여줄게 #쇼츠 #shorts")
    private String title;

    @Schema(description = "내용", example = "#드림노트 #보여줄게 #쇼츠 #shorts 내용")
    private String content;

    @Schema(description = "이미지", example = "없으면 0 or 공백")
    private String image;

    @Schema(description = "비디오", example = "f3e633284f7ade654c6767f5e53c5afe")
    private String video;

    @Schema(description = "is_view", example = "1")
    private Integer isView;

    @Schema(description = "scvYn", example = "0")
    private Integer scvYn;

    @Schema(description = "View Count", example = "999")
    private Integer viewCount;

    @Schema(description = "Good Count", example = "138")
    private Integer goodCount;

    @Schema(description = "Bad Count", example = "138")
    private Integer badCount;

    @Schema(description = "Reply Count", example = "138")
    private Integer replyCount;

    @Schema(description = "생성 일자", example = "2023-02-14 23:48:14")
    private LocalDateTime createdDate;

    @Schema(description = "수정 일자", example = "2023-02-14 23:48:14")
    private LocalDateTime updateDate;

    @Schema(description = "추천 비추천 타입", example = "good or bad")
    private String likeType;

    @Schema(description = "좋아요 여부", example = "0 or 1")
    private String fav;

    @Schema(description = "사용자 블럭 여부", example = "false")
    private boolean isUserBlock;

    @Schema(description = "Piece Block YN", example = "false")
    private boolean isPieceBlock;
}
