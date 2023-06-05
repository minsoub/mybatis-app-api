package kr.co.fns.app.api.minute.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Minute Link type 데이터")
public class MinuteLinkType {

    @Schema(description = "Minute Like Type", example = "none")
    private String likeType;

    @Schema(description = "Minute Favorite 여부", example = "1")
    private int favType;
}
