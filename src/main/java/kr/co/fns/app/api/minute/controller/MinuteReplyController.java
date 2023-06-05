package kr.co.fns.app.api.minute.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.fns.app.api.minute.model.request.MinuteReplyInsertRequest;
import kr.co.fns.app.api.minute.model.response.MinuteDetailResponse;
import kr.co.fns.app.api.minute.model.response.MinuteReplyListResponse;
import kr.co.fns.app.api.minute.service.MinuteReplyService;
import kr.co.fns.app.base.dto.GeneralResponse;
import kr.co.fns.app.config.resolver.Account;
import kr.co.fns.app.config.resolver.CurrentUser;
import kr.co.fns.app.core.aop.exception.ErrorCode;
import kr.co.fns.app.core.aop.exception.custom.FantooException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigInteger;
import java.util.Locale;

@Tag(name = "Minute Reply API", description = "미닛 댓글 API 목록")
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/minute")
@RestController
public class MinuteReplyController {
    private final MinuteReplyService minuteReplyService;

    @Operation(summary = "minute, 미닛 댓글 리스트 출력", description="미닛 댓글 리스트를 출력한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = MinuteDetailResponse.class)))
    })
    @GetMapping("/{idx}/reply")
    public GeneralResponse<MinuteReplyListResponse> minuteDetail(
            Locale locale,
            @Parameter(description = "Minute 번호", example = "45") @PathVariable(name = "idx") BigInteger idx,
            @Parameter(description = "통합정보 UID", example = "xx_u_2593789567d2f5d2adf5a11eda6f27538ce5e4dxx") @RequestParam(value = "integUid", required = false) String integUid,
            @Parameter(description = "페이지번호", example = "1") @RequestParam(value = "page", required = false) @Min(0) Integer page,
            @Parameter(description = "페이징 처리여부", example = "false") @RequestParam(value = "nextCheck", required = false) String nextCheck,
            @Parameter(description = "Top Index", example = "1000") @RequestParam(value = "topIdx", required = false) @Min(0) Integer topIdx,
            @Parameter(description = "Order", example = "good_count") @RequestParam(value = "order", required = false) String order,
            @Parameter(description = "Sub Type", example = "minute") @RequestParam(value = "subType", required = false) String subType
    ) {

        boolean isNextCheck = false;
        int intTopIdx = 0;
        int intPage = 0;
        if (!StringUtils.isEmpty(nextCheck)) {
            isNextCheck = Boolean.parseBoolean(nextCheck);
        }
        if (StringUtils.isEmpty(integUid)) integUid = "";
        if (StringUtils.isEmpty(subType)) subType = "minute"; // default
        if (topIdx != null) intTopIdx = topIdx.intValue();
        if (page != null) intPage = page.intValue();

        MinuteReplyListResponse minuteDetailResponse = minuteReplyService.getMinuteReplyList(idx, integUid, intPage, isNextCheck, intTopIdx, order, subType);

        return new GeneralResponse<>(minuteDetailResponse);
    }

    @Operation(summary = "minute, 댓글 신규 등록 API", description = "미닛 댓글 신규 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/{idx}/reply")
    public GeneralResponse<String> minuteReplyWrite(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "Minute 번호", example = "45") @PathVariable(name = "idx") BigInteger idx,
            @Parameter(description = "미닛 댓글 등록") @Valid @RequestBody MinuteReplyInsertRequest minuteReplyRequest
    ) {
        if (!account.getIntegUid().equals(minuteReplyRequest.getIntegUid())) {
            throw new FantooException(ErrorCode.AccessDeniedException);
        }
        String result = minuteReplyService.insertMinuteReply(locale, idx, account, minuteReplyRequest);


        return new GeneralResponse<>(result);
    }

    @Operation(summary = "minute, 댓글 삭제 API", description = "미닛 댓글 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/{idx}/reply/{replyIdx}")
    public GeneralResponse<String> minuteReplyDelete(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "통합정보 UID", example = "통합정보 UID") @RequestParam(value = "integUid", required = true) String integUid,
            @Parameter(description = "Minute 번호", example = "867") @PathVariable(name = "idx") BigInteger idx,
            @Parameter(description = "Minute 댓글 번호", example = "258") @PathVariable(name = "replyIdx") BigInteger replyIdx
    ) {
        if (!account.getIntegUid().equals(integUid)) {
            throw new FantooException(ErrorCode.AccessDeniedException);
        }
        String result = minuteReplyService.deleteMinuteReply(idx, replyIdx, account.getIntegUid());

        return new GeneralResponse<>(result);
    }

    @Operation(summary = "minute, 댓글 Count 동기화 API", description = "미닛 댓글 Count를 동기화한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/{idx}/reply/sync")
    public GeneralResponse<String> minuteReplySync(
            Locale locale,
            @Parameter(description = "Minute 번호", example = "45") @PathVariable(name = "idx") BigInteger idx
    ) {
        String result = minuteReplyService.minuteReplySync(idx);

        return new GeneralResponse<>(result);
    }
}
