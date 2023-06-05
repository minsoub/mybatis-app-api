package kr.co.fns.app.api.minute.model.response;


import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.api.minute.entity.MinuteList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Minute List 데이터 응답")
public class MinuteListResponse {

    @Schema(description = "Admin 여부", example = "true or false")
    private Boolean admin;

    @Schema(description = "Total Page", example = "10")
    private int totalPage;

    @Schema(description = "미닛 List 정보", example = "미닛 리스트 (Schema 참조)") // , implementation = MinuteList.class)
    private List<MinuteList> minuteList;
}
