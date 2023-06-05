package kr.co.fns.app.base.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralResponse<T> extends ApiResult {
    private T dataObj;

    public GeneralResponse(T data) {
        super();
        this.dataObj = data;
    }

    public GeneralResponse() {
        super();
    }
}
