package kr.co.fns.app.core.paging.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.fns.app.base.dto.IntegUidDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 *
 * PagingDto
 *
 * @file PagingDto.java
 * @author ParkSeongHyun
 * @since 2022.05.23
 * @desc 통합 페이징관련 Dto
 */


@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingDto extends IntegUidDto {

    @Schema(description = "페이징 실행유무", example = "false", required = true)
    private boolean pagingStatus;

    @Schema(description = "페이징 실행시 데이터", example = "", required = false)
    private String pagingData;

    @Schema(description = "페이징 데이터 갯수", example = "10", required = true)
    private int limit;
}
