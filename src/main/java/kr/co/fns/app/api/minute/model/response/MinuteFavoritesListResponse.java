package kr.co.fns.app.api.minute.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.api.minute.entity.MinuteFavoritesDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Minute 즐겨찾기 List 데이터 응답")
public class MinuteFavoritesListResponse {
    @Schema(description = "Total Page", example = "10")
    private int totalPage;

    @Schema(description = "미닛 즐겨찾기 List 정보", example = "미닛 즐겨찾기 리스트 (Schema 참조)")
    private List<MinuteFavoritesDetail> minuteList;
}
