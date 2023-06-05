package kr.co.fns.app.api.minute.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Minute Count type 상세 데이터")
public class MinuteResult {
    @Schema(description = "Minute 번호", example = "34")
    private BigInteger idx;

    @Schema(description = "View Count", example = "999")
    private Integer viewCount;

    @Schema(description = "Good Count", example = "138")
    private Integer goodCount;

    @Schema(description = "Bad Count", example = "138")
    private Integer badCount;

    @Schema(description = "Reply Count", example = "138")
    private Integer replyCount;
}
