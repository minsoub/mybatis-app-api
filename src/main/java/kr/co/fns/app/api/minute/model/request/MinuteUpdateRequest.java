package kr.co.fns.app.api.minute.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MinuteUpdateRequest {
    @JsonIgnore
    private BigInteger idx;

    @Schema(description = "통합 ID", example = "ft_u_f96a7595c13a11ecb6386f262a44c38b_xxxxx")
    private String integUid;

    @Schema(description = "언어코드", example = "ko", required = true)
    private String lang;

    @Schema(description = "사용자 ID", example = "fantootv@fns.ai", required = true)
    private String userId;

    @Schema(description = "사용자 NickName", example = "FantooTV", required = false)
    private String userNickname;

    @Schema(description = "Video", example = "a193d17a6486418785b83128521cc327", required = true)
    private String video;

    @Schema(description = "제목", example = "#PIXY #픽시 #쇼츠 #shorts #Tears", required = true)
    private String title;

    @Schema(description = "내용", example = "내용", required = false)
    private String content;

    @Schema(description = "이미지", example = "이미지명", required = false)
    private String image;
}
