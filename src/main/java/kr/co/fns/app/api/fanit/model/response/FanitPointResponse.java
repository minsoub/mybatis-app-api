package kr.co.fns.app.api.fanit.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.api.fanit.entity.FanitList;
import kr.co.fns.app.api.fanit.entity.FanitPoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Fanit 포인트 데이터 응답")
public class FanitPointResponse {

    @Schema(description = "Fanit 포인트 데이터", example = "FanitPoint 객체 (Schema 참조)", implementation = FanitPoint.class)
    private List<FanitPoint> fanitPointList;

}
