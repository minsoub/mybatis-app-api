package kr.co.fns.app.api.minute.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Minute 마지막 작성자")
public class MinuteLastResponse {
    @Schema(description = "통합 ID", example = "ft_u_f96a7595c13a11ecb6386f262a44c38b_xxxxx")
    private String integUid;
}
