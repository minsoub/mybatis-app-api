package kr.co.fns.app.api.fanit.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.api.fanit.entity.FanitList;
import kr.co.fns.app.api.fanit.entity.FanitListDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Fanit 리스트 데이터 응답")
public class FanitListDetailResponse {

    @Schema(description = "Fanit 데이터", example = "FanitList 객체 (Schema 참조)", implementation = FanitList.class)
    private FanitList fanitDetail;

}
