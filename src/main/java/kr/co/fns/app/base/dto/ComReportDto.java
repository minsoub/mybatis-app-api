package kr.co.fns.app.base.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.fns.app.base.type.ComTargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComReportDto {
    /**
     * 커뮤니티 게시글 or 댓글 신고
     */

    @JsonIgnore
    private ComTargetType comTargetType;

    // 해당 게시물 or 댓글 pk id
    @JsonIgnore
    private int targetId;

    // 신고 메세지 pk id
    private int reportMessageId;

    // 해당 게시물 integUid
    private String targetIntegUid;
}
