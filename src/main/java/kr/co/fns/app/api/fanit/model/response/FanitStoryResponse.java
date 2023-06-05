package kr.co.fns.app.api.fanit.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.api.fanit.entity.FanitPoint;
import kr.co.fns.app.api.fanit.entity.FanitStory;
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
public class FanitStoryResponse {

    @Schema(description = "Fanit Story 데이터", example = "FanitStory 객체 (Schema 참조)", implementation = FanitStory.class)
    private List<FanitStory> fanitStoryList;

}
