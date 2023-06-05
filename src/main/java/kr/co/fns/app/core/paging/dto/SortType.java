package kr.co.fns.app.core.paging.dto;

/**
 * 22/03/28
 * 정렬 Type, 페이징시 사용
 */
public enum SortType {

    ASC("ASC"),
    DESC("DESC");

    private final String type;

    SortType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
