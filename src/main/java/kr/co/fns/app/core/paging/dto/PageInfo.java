package kr.co.fns.app.core.paging.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.session.RowBounds;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"offset", "limit"})
public class PageInfo extends RowBounds {

    @JsonIgnore
//    @NotEmpty(message = "이메일은 필수 입력값입니다.")
    private Integer page = 0; // page 번호

    @JsonIgnore
    private Integer size = 10; // 노출 size

    @JsonIgnore
    protected Long totalCount = -1L; // set이 되지않았다는 의미로 -1

    @JsonIgnore
    private Boolean pagingStatus = false; // PageInfo를 상속받았더라도  pagingStatus = true를 해야 페이징 intercepter로 잡힌다. , false: 페이징 사용안함.

    @JsonIgnore
    private String sort; // order by에 쓰일 컬럼명을 적는다.

    @JsonIgnore
    private SortType sortType = SortType.ASC; // 정렬 DESC, ASC , default ASC

}
