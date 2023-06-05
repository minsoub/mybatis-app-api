package kr.co.fns.app.api.minute.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.api.minute.entity.MinuteDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Minute 상세 데이터 응답")
public class MinuteDetailResponse {

    @Schema(description = "Admin 여부", example = "true or false")
    private Boolean admin;

    @Schema(description = "미닛 상세 정보", example = "MinuteDetail 객체 (Schema 참조)", implementation = MinuteDetail.class)
    private MinuteDetail minuteDetail;
}
