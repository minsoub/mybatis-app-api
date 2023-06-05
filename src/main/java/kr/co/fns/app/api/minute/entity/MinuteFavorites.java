package kr.co.fns.app.api.minute.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Minute Favorites 데이터")
public class MinuteFavorites {
    @Schema(description = "Minute Favorites 번호", example = "34")
    private BigInteger idx;

    @Schema(description = "Minute 번호", example = "34")
    private BigInteger subIdx;

    @Schema(description = "통합 ID", example = "ft_u_f96a7595c13a11ecb6386f262a44c38b_xxxxx")
    private String integUid;

    @Schema(description = "생성 일자", example = "2023-02-14 23:48:14")
    private LocalDateTime updatedDate;
}
