package kr.co.fns.app.api.minute.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Minute Hashtag")
public class MinuteHashtag {
    @JsonIgnore
    @Schema(description = "Minute Hashtag 번호", example = "1")
    private BigInteger minuteHashtagId;

    @Schema(description = "Minute 번호", example = "34")
    private BigInteger minuteId;

    @Schema(description = "Minute Tag", example = "클럽")
    private String tag;
}
