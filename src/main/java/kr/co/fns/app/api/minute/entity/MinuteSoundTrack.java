package kr.co.fns.app.api.minute.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Minute SoundTrack 데이터")
public class MinuteSoundTrack {
    @Schema(description = "음원 번호", example = "1")
    private BigInteger minuteSongId;

    @Schema(description = "음원 타이틀", example = "맨발의 청춘")
    private String songTitle;

    @Schema(description = "음원 아티스트", example = "최봉진")
    private String songArtist;

    @Schema(description = "음원 설명", example = "신나는 음원임")
    private String songDesc;

    @Schema(description = "실제 음원 파일명", example = "testmusic.mp3")
    private String realFileName;

    @Schema(description = "표시 음원 파일명", example = "testmusic.mp3")
    private String dispFileName;

    @Schema(description = "음원 재생시간", example = "00:00:00")
    private LocalTime songPlayTime;
}
