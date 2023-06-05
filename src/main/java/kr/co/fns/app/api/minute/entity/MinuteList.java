package kr.co.fns.app.api.minute.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Minute List 데이터")
public class MinuteList extends MinuteDetail {
    @Schema(description = "User Nickname", example = "한류타임스")
    private String userNickname;

    @Schema(description = "User Photo", example = "073b9b7-3896-42ec-9a2e-e6d3791d9b00")
    private String userPhoto;

    @Schema(description = "Sort Value", example = "2345.32")
    private double sortValue;

    @Schema(description = "Origin Value", example = "1023.21")
    private double originValue;

    @Schema(description = "해시태그 리스트(,로 구분)", example = "태그1, 태그2, 태그3")
    private String hashtagList;

    @Schema(description = "음원 사용여부 - Remake 버튼 여부", example = "true")
    private boolean isSoundTrack;

    @Schema(description = "음원 IDX", example = "1")
    private Integer minuteSongId;

}
