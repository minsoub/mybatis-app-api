package kr.co.fns.app.api.country.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "국가데이터를 공통으로 쓰기위해 생성")
public class CountryData {
    @Schema(description = "국가")
    private String country;

    @Schema(description = "국가 코드")
    private String countryCode;

}
