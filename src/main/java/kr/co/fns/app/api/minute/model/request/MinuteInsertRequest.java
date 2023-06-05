package kr.co.fns.app.api.minute.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Minute 신규 등록 데이터")
public class MinuteInsertRequest {
    @JsonIgnore
    private BigInteger idx;

    @Schema(description = "통합 ID", example = "ft_u_f96a7595c13a11ecb6386f262a44c38b_xxxxx", required = true)
    private String integUid;

    @Schema(description = "언어코드", example = "ko", required = true)
    private String lang;

    @Schema(description = "Video", example = "a193d17a6486418785b83128521cc327", required = true)
    private String video;

    @Schema(description = "공개여부", example = "1", required = true)
    private String isView;

    @Schema(description = "제목", example = "#PIXY #픽시 #쇼츠 #shorts #Tears", required = true)
    private String title;

    @Schema(description = "사용자 ID", example = "fantootv@fns.ai", required = false)
    private String userId;

    @Schema(description = "사용자 NickName", example = "FantooTV", required = false)
    private String userNickname;

    @Schema(description = "사용자 이미지", example = "uploads/profile-1676278366106.jpg")
    private String userPhoto;

    @Schema(description = "내용", example = "내용", required = false)
    private String content;

    @Schema(description = "이미지", example = "이미지명", required = false)
    private String image;

    @Schema(description = "음원 번호", example = "4", required = false)
    private BigInteger minuteSongId;

    @Size(max=5, message = "ERROR_FE4016")
    @Schema(description = "해시태그", example = "[\"판투\", \"이미지\"]", required = false)
    private List<String> hashtagList;
}
