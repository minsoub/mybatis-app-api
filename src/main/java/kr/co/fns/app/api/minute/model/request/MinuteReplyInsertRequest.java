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
public class MinuteReplyInsertRequest {
    @JsonIgnore
    private BigInteger idx;

    @JsonIgnore
    private BigInteger subIdx;

    @JsonIgnore
    private String subType = "minute";     // default: minute

    @JsonIgnore
    private int anonymous = 0;

    @JsonIgnore
    private int isView = 1;  // 1 : View

    @Schema(description = "통합 ID", example = "ft_u_f96a7595c13a11ecb6386f262a44c38b_xxxxx", required = true)
    private String integUid;

    @Schema(description = "내용", example = "내용", required = true)
    private String comment;

    @Schema(description = "사용자 ID", example = "fantootv@fns.ai", required = false)
    private String userId;

    @Schema(description = "사용자 NickName", example = "FantooTV", required = false)
    private String userNickname;

    @Schema(description = "사용자 이미지", example = "uploads/profile-1676278366106.jpg")
    private String userPhoto;
}
