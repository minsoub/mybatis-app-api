package kr.co.fns.app.api.minute.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.fns.app.api.minute.entity.MinuteSoundTrack;
import kr.co.fns.app.api.minute.model.response.MinuteListResponse;
import kr.co.fns.app.api.minute.service.MinuteSoundTrackService;
import kr.co.fns.app.base.dto.GeneralResponse;
import kr.co.fns.app.config.resolver.Account;
import kr.co.fns.app.config.resolver.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Locale;

@Tag(name = "Minute 음원 API", description = "미닛 음원 API 목록")
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/minute/soundtrack")
@RestController
public class MinuteSoundTrackController {

    private final MinuteSoundTrackService minuteSoundTrackService;

    @Operation(summary = "minute, 미닛 음원 상세정보 조회 API", description = "등록된 음원의 상세 정보를 리턴한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = MinuteSoundTrack.class)))
    })
    @GetMapping("/{idx}")
    public GeneralResponse<MinuteSoundTrack> soundTrackDetail(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "SoundTrack idx", example = "4") @PathVariable(name = "idx") BigInteger minuteSongId
    ) {
        MinuteSoundTrack result = minuteSoundTrackService.soundTrackDetail(account.getIntegUid(), minuteSongId);

        return new GeneralResponse<>(result);
    }

    @Operation(summary = "minute, 음원과 연결된 미닛 리스트 조회 API", description = "등록된 음원의 상세 정보를 리턴한다. 통합 UID가 파라미터로 넘어온 경우 해당 음원을 사용하는 나의 미닛 리스트를 리턴한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = MinuteListResponse.class)))
    })
    @GetMapping("/{idx}/used")
    public GeneralResponse<MinuteListResponse> minuteSoundTrackList(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "SoundTrack idx", example = "4") @PathVariable(name = "idx") BigInteger minuteSongId,
            @Parameter(description = "페이지번호", example = "1") @RequestParam(value = "page") int page,
            @Parameter(description = "페이징 처리여부", example = "false") @RequestParam(value = "nextCheck", required = false) String nextCheck
    ) {
        boolean isNextCheck = false;
        if (!StringUtils.isEmpty(nextCheck)) {
            isNextCheck = Boolean.parseBoolean(nextCheck);
        }

        MinuteListResponse result = minuteSoundTrackService.minuteSoundTrackList(account.getIntegUid(), minuteSongId, page, isNextCheck);

        return new GeneralResponse<>(result);
    }
}
