package kr.co.fns.app.api.fanit.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Fanit 리스트 디테일 데이터")
public class FanitListDetail {
    @Schema(description = "Fanit 번호 ", example = "99")
    private Integer fanitDetailId;

    @Schema(description = "팬아이트 ID")
    private Integer fanitId;

    @Schema(description = "팬아이트 속성 제목", example = "속성 제목")
    private String fanitPropTitle;

    @Schema(description = "팬아이트 속성 이름", example = "속성 이름")
    private String fanitPropName;

    @Schema(description = "팬아이트 속성 이미지", example = "image.jpg")
    private String fanitPropImg;

    @Schema(description = "총 개수")
    private int voteCnt;

    @Schema(description = "서비스 여부")
    private Integer svcYn;

    @Schema(description = "생성 일자", example = "2021-09-03 17:20:36")
    private String createDate;

    @Schema(description = "팬아이트 속성 이미지(Cloudflare)", example = "prop_image_cdn.jpg")
    private String fanitPropImgCloudflare;

    @Schema(description = "총 범위")
    private double voteRate;
}