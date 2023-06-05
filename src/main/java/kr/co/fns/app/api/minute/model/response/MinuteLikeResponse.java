package kr.co.fns.app.api.minute.model.response;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.api.minute.entity.MinuteLinkType;
import kr.co.fns.app.api.minute.entity.MinuteResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Minute Like 설정 리턴 데이터")
public class MinuteLikeResponse {

    @Schema(description = "미닛 Count 항목 결과", example = "미닛 Count 항목 (Schema 참조)")
    private MinuteResult returnResult;

    @Schema(description = "미닛 Like Type 결과", example = "미닛 Like Type  (Schema 참조)")
    private MinuteLinkType countResult;
}
