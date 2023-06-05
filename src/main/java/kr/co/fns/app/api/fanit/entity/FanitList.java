package kr.co.fns.app.api.fanit.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Fanit 데이터")
public class FanitList {
    @Schema(description = "Fanit 번호", example = "99")
    private Integer idx;

    @Schema(description = "Fanit 타입", example = "event")
    private String type;

    @Schema(description = "Fanit 투표 제목", example = "팬아이트 타이틀")
    private String fanitTitle;

    @Schema(description = "Fanit 투표 상세 설명")
    private String fanitDesc;

    @Schema(description = "배너 이미지", example = "image.jpg")
    private String bannerImg;

    @Schema(description = "혜택 이미지", example = "benefit.jpg")
    private String benefitImg;

    @Schema(description = "혜택 내용 텍스트", example = "아이돌 생일 추첨 혜택")
    private String benefitText;

    @Schema(description = "사용 여부", example = "사용여부,  0: 종료 , 1: 진행 , 2 : 중단")
    private Integer svcYn;

    @Schema(description = "시작 일자", example = "2023-01-01")
    private String startDate;

    @Schema(description = "종료 일자", example = "2023-12-31")
    private String endDate;

    @Schema(description = "생성자", example = "admin")
    private String createUser;

    @Schema(description = "생성 일자", example = "2021-11-04 10:10:24")
    private String createDate;

    @Schema(description = "서비스 여부", example = "0 or 1")
    private Integer status;

    @Schema(description = "혜택 이미지(Cloudflare)")
    private String benefitImgCloudflare;

    @Schema(description = "배너 이미지(Cloudflare)")
    private String bannerImgCloudflare;

    @Schema(description = "Fanit 리스트 데이터 토탈카운트", example = "89718793")
    private int totalVoteCnt;

    @Schema(description = "Fanit 토탈 스토리 데이터카운트", example = "32")
    private int totalStoryCnt;

    @Schema(description = "Fanit 리스트 데이터", example = "FanitListDetail 객체 (Schema 참조)", implementation = FanitListDetail.class)
    private List<FanitListDetail> fanitListDetail;

}
