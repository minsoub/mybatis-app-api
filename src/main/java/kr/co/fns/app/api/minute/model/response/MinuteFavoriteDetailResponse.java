package kr.co.fns.app.api.minute.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.api.minute.entity.MinuteFavorites;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Minute Favorite 설정 여부 및 상세 데이터")
public class MinuteFavoriteDetailResponse {
    @Schema(description = "미닛 즐겨찾기 등록 여부", example = "true")
    private boolean isFavorite;

    @Schema(description = "미닛 즐겨찾기 상세 정보", example = "미닛 즐겨찾기 상세 정보 (Schema 참조)")
    private MinuteFavorites minuteFavorites;
}
