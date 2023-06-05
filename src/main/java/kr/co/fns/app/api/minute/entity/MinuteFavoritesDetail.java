package kr.co.fns.app.api.minute.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.api.minute.model.enums.LikeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Minute Favorites 상세 데이터")
public class MinuteFavoritesDetail {

    @Schema(description = "Minute 번호", example = "34")
    private BigInteger idx;

    @Schema(description = "통합 ID", example = "ft_u_f96a7595c13a11ecb6386f262a44c38b_xxxxx")
    private String integUid;

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

    @Schema(description = "사용자 Nickname", example = "짱")
    private String userNickname;

    @Schema(description = "사용자 Photo", example = "uploads/profile-1677330258307.jpg")
    private String userPhoto;

    @Schema(description = "추천 비추천 타입", example = "good/bad/none")
    private LikeType likeType;

    @Schema(description = "Favorites 여부", example = "1")
    private Integer fav;

    @Schema(description = "해시태그 리스트(,로 구분)", example = "태그1, 태그2, 태그3")
    private String hashtagList;

    @Schema(description = "음원 사용여부 - Remake 버튼 여부", example = "true")
    private boolean isSoundTrack;

    @Schema(description = "음원 IDX", example = "1")
    private Integer minuteSongId;
}
