package kr.co.fns.app.api.minute.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.api.minute.entity.MinuteList;
import kr.co.fns.app.api.minute.entity.MinuteReply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Minute Reply List 데이터 응답")
public class MinuteReplyListResponse {
    @Schema(description = "Total Page", example = "10")
    private int totalPage;

    @Schema(description = "Total Length", example = "100")
    private int totalLength;

    @Schema(description = "미닛 Reply List 정보", example = "미닛 Reply 리스트 (Schema 참조)") // , implementation = MinuteList.class)
    private List<MinuteReply> minuteReplyList;
}
