package kr.co.fns.app.api.event.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.api.event.entity.EventStory;
import kr.co.fns.app.api.fanit.entity.FanitList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "댓글 event 응답")
public class EventResponse {

    @Schema(description = "댓글 event 데이터", example = "EventStory 객체 (Schema 참조)", implementation = EventStory.class)
    private List<EventStory> eventStoryList;

    @Schema(description = "Total Count", example = "100")
    private int totalCnt;

    @Schema(description = "Total Page", example = "10")
    private int totalPage;

}
