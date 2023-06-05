package kr.co.fns.app.api.event.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Event 메시지 응답")
public class EventMessageResponse {

    @Schema(description = "결과값 카운트", example = "1")
    private int resultCnt;

    @Schema(description = "event story id", example = "1")
    private int eventStoryId;

    @Schema(description = "event story reply id", example = "1")
    private int eventStoryReplyId;

}
