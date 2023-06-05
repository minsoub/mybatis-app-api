package kr.co.fns.app.base.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseSearchDto {
	// return용 Paging 결과
	private int page;

	private int size;

	private Long totalCount;
}
