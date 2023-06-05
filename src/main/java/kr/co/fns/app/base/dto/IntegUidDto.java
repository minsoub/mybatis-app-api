package kr.co.fns.app.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntegUidDto {
    @Schema(description = "회원의 통합ID", hidden = true, required = false)
    private String integUid;
}
