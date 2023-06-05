package kr.co.fns.app.api.fanit.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.api.fanit.entity.FanitList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Fanit 메시지 응답")
public class FanitMessageResponse {

    @Schema(description = "보유 포인트", example = "10")
    private int point;

    @Schema(description = "사용한 포인트", example = "10")
    private int usepoint;

    @Schema(description = "오늘 사용할수 있는 포인트", example = "10")
    private int remindPoint;

    @Schema(description = "결과값 카운트", example = "1")
    private int resultCnt;

    @Schema(description = "fanit story id", example = "1")
    private int fanitStoryId;

}
