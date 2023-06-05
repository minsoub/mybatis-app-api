package kr.co.fns.app.api.minute.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.fns.app.api.minute.model.enums.LikeType;
import kr.co.fns.app.api.minute.model.enums.SubType;
import kr.co.fns.app.api.minute.model.request.MinuteUpdateRequest;
import kr.co.fns.app.api.minute.model.response.MinuteLikeResponse;
import kr.co.fns.app.api.minute.service.MinuteLikeService;
import kr.co.fns.app.api.minute.service.MinuteReplyService;
import kr.co.fns.app.base.dto.GeneralResponse;
import kr.co.fns.app.config.resolver.Account;
import kr.co.fns.app.config.resolver.CurrentUser;
import kr.co.fns.app.core.aop.exception.ErrorCode;
import kr.co.fns.app.core.aop.exception.custom.FantooException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.Locale;

@Tag(name = "Minute Like API", description = "미닛 Like API 목록")
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/minute")
@RestController
public class MinuteLikeController {
    private final MinuteLikeService minuteLikeService;

    @Operation(summary = "minute, 미닛 Like 설정 API", description = "등록된 미닛의 Like를 설정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/{idx}/like")
    public GeneralResponse<MinuteLikeResponse> minuteLike(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "통합정보 UID", example = "통합정보 UID") @RequestParam(value = "integUid") String integUid,
            @Parameter(description = "Minute idx", example = "60") @PathVariable(name = "idx") BigInteger idx,
            @Parameter(description = "Sub Type", example = "minute") @RequestParam(value = "subType") SubType subType,
            @Parameter(description = "Like Type", example = "good") @RequestParam(value = "likeType") LikeType likeType
    ) {
        if (!account.getIntegUid().equals(integUid)) {
            throw new FantooException(ErrorCode.AccessDeniedException);
        }
        MinuteLikeResponse result = minuteLikeService.setMinuteLike(locale, account, idx, subType, likeType);

        return new GeneralResponse<>(result);
    }

    @Operation(summary = "minute, 미닛 Like Check API", description = "등록된 미닛의 Like Check 상태 리턴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{idx}/like")
    public GeneralResponse<String> isMinuteLike(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "통합정보 UID", example = "통합정보 UID") @RequestParam(value = "integUid") String integUid,
            @Parameter(description = "Minute idx", example = "60") @PathVariable(name = "idx") BigInteger idx,
            @Parameter(description = "Sub Type", example = "minute") @RequestParam(value = "subType") SubType subType
    ) {
        if (!account.getIntegUid().equals(integUid)) {
            throw new FantooException(ErrorCode.AccessDeniedException);
        }
        String result = minuteLikeService.currentMinuteLike(account.getIntegUid(), idx, subType);

        return new GeneralResponse<>(result);
    }
}
