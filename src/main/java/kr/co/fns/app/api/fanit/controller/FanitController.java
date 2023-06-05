package kr.co.fns.app.api.fanit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.fns.app.api.fanit.model.response.FanitListDetailResponse;
import kr.co.fns.app.api.fanit.model.response.FanitListResponse;
import kr.co.fns.app.api.fanit.model.response.FanitMessageResponse;
import kr.co.fns.app.api.fanit.model.response.FanitPointResponse;
import kr.co.fns.app.api.fanit.service.FanitService;
import kr.co.fns.app.base.dto.GeneralResponse;
import kr.co.fns.app.config.resolver.Account;
import kr.co.fns.app.config.resolver.CurrentUser;
import kr.co.fns.app.core.aop.exception.ErrorCode;
import kr.co.fns.app.core.aop.exception.custom.FantooException;
import kr.co.fns.app.core.receiver.enums.RewardType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.util.Locale;

@ApiResponses({
        @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = GeneralResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘 못된 접근"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
})
@Tag(name = "Fanit API", description = "팬잇 관련 API 목록")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/fanit")
@RestController
public class FanitController {

    private final FanitService fanitService;


    @GetMapping("")
    @Operation(summary = "fanit 투표 리스트 API (access_token 필수)", description="")
    public GeneralResponse<FanitListResponse> getList(
            Locale locale,
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(value = "page", required = false) int page
    ) {
        FanitListResponse fanitListResponse = fanitService.getFanitPro(page);

        return new GeneralResponse<>(fanitListResponse);
    }

    @PostMapping("")
    @Operation(summary = "팬잇 지급 API (access_token 필수)", description="")
    public GeneralResponse<String> fanitSave(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "통합정보 UID", example = "ft_u_0096359625c9863edea1f11edb390ad3abdb0cf95") @RequestParam(value = "integUid") String integUid,
            @Parameter(description = "보상타입", example = "FANTOO_TV_VIEW_REWARD") @RequestParam(value = "rewordType") RewardType rewardType,
            @Parameter(description = "참조(Fantoo TV ID)", example = "YYD6JPHC2C") @RequestParam(value = "refId", required = true) String refId
    ) {
        if (!account.getIntegUid().equals(integUid)) {
            throw new FantooException(ErrorCode.AccessDeniedException);
        }
        log.debug("locale => {}, {}, {}, {}", locale.getCountry(), locale.getDisplayName(), locale.getDisplayCountry(), locale.getDisplayLanguage());
        String result = fanitService.saveFanit(account,integUid,rewardType,refId);
        // TODO: 황우진 작업진행되면 진행 예정
        return new GeneralResponse<>(result);
    }

    @GetMapping("/{idx}")
    @Operation(summary = "fanit 투표 리스트 API", description="")
    public GeneralResponse<FanitListDetailResponse> getFanit(
            Locale locale,
            @Parameter(description = "fanit 번호", example = "99") @PathVariable(name = "idx", required = true) int idx
    ) {
        FanitListDetailResponse fanitListDetailResponse = fanitService.getFanitDetail(idx);

        return new GeneralResponse<>(fanitListDetailResponse);
    }

    @PatchMapping("/{idx}")
    @Operation(summary = "팬잇 투표수 총합계 보정용 API", description="fanit 번호를 받아서 실행되는 API")
    public GeneralResponse<FanitListDetailResponse> modifyFanit(
            Locale locale,
            @Parameter(description = "fanit 번호", example = "99") @PathVariable(name = "idx", required = true) int idx
    ) {
        FanitListDetailResponse fanitListDetailResponse = fanitService.updateFanit(idx);
        return new GeneralResponse<>(fanitListDetailResponse);

    }

    @PatchMapping("")
    @Operation(summary = "팬잇 투표수 총합계 보정용 API", description="매 10분마다 현재 투표중인 팬잇 투표내역에서 후보자별 팬잇 총합을 갱신 중하여 사용중임.")
    public GeneralResponse<FanitListResponse> modifyFanitList(
            Locale locale
    ) {
        FanitListResponse fanitListResponse = fanitService.updateFanitList();
        return new GeneralResponse<>(fanitListResponse);

    }
    @PostMapping("/vote")
    @Operation(summary = "팬잇 투표 이벤트 투표하기 API (access_token 필수)", description="")
    public GeneralResponse<FanitMessageResponse> fanitVote(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "통합정보 UID", example = "ft_u_0096359625c9863edea1f11edb390ad3abdb0cf95") @RequestParam(value = "integUid", required = true) String integUid,
            @Parameter(description = "fanit 번호", example = "100") @RequestParam(value = "fanitId", required = true) int fanitId,
            @Parameter(description = "fanit 상세 번호", example = "2667") @RequestParam(value = "fanitDetailId", required = true) int fanitDetailId,
            @Parameter(description = "point", example = "1") @Size(min = 1, max=1000000) @RequestParam(value = "point", required = true) int point

    ) {
        FanitMessageResponse fanitMessageResponse = fanitService.updateFanitVote(account,integUid,fanitId,fanitDetailId,point);
        return new GeneralResponse<>(fanitMessageResponse);

    }

    @PostMapping("/vote/amofe2023")
    @Operation(summary = "팬잇 투표 이벤트 투표하기 API (access_token 필수)", description="")
    public GeneralResponse<FanitMessageResponse> fanitVote(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "통합정보 UID", example = "ft_u_0096359625c9863edea1f11edb390ad3abdb0cf95") @RequestParam(value = "integUid", required = true) String integUid,
            @Parameter(description = "투표포인트 model Id", example = "YYD6JPHC2C") @RequestParam(value = "modelId", required = true) String modelId
    ) {
        FanitMessageResponse fanitMessageResponse = fanitService.updateFanitVoteAmofe(account,integUid,modelId);
        return new GeneralResponse<>(fanitMessageResponse);

    }
    @GetMapping("/vote/check")
    @Operation(summary = "팬잇 투표 가능 여부 확인 API", description="")
    public GeneralResponse<String> voteCheck(
            Locale locale,
            @Parameter(description = "fanit 번호", example = "107") @RequestParam(value = "fanitId", required = true) int fanitId
    ) {
        String result = fanitService.getFanitVote(fanitId);
        return new GeneralResponse<>(result);
    }

    @PostMapping("/save")
    @Operation(summary = "팬잇 투표페이지 진입시 하루 1회 10포인트 증정 API (access_token 필수)", description="")
    public GeneralResponse<FanitMessageResponse> savePoint(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "통합정보 UID", example = "ft_u_0096359625c9863edea1f11edb390ad3abdb0cf95") @RequestParam(value = "integUid", required = true) String integUid
    ) {
        FanitMessageResponse fanitMessageResponse = fanitService.saveFanitVote(account,integUid);
        return new GeneralResponse<>(fanitMessageResponse);
    }

    @PostMapping("/save/amofe2023")
    @Operation(summary = "아모페 2023 용 팬잇 적립 API (access_token 필수)", description="")
    public GeneralResponse<FanitMessageResponse> amofeSavePoint(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "통합정보 UID", example = "ft_u_0096359625c9863edea1f11edb390ad3abdb0cf95") @RequestParam(value = "integUid", required = true) String integUid
    ) {
        FanitMessageResponse fanitMessageResponse = fanitService.amofeSavePoint(account,integUid);
        return new GeneralResponse<>(fanitMessageResponse);
    }

    @GetMapping("/mypoint")
    @Operation(summary = "나의 팬잇 포인트 확인용 API (access_token 필수)", description="")
    public GeneralResponse<FanitMessageResponse> mypoint(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "통합정보 UID", example = "ft_u_0096359625c9863edea1f11edb390ad3abdb0cf95") @RequestParam(value = "integUid", required = true) String integUid
    ) {
        FanitMessageResponse fanitMessageResponse = fanitService.getMyPoint(account,integUid);
        return new GeneralResponse<>(fanitMessageResponse);
    }

    @GetMapping("/mypoint/history")
    @Operation(summary = "개인별 포인트 적립내역 확인을 위한 API (access_token 필수)", description="")
    public GeneralResponse<FanitPointResponse> mypointHistory(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "통합정보 UID", example = "ft_u_0096359625c9863edea1f11edb390ad3abdb0cf95") @RequestParam(value = "integUid", required = true) String integUid,
            @Parameter(description = "포인트 사용내역 type", schema = @Schema(
                    allowableValues = {"0", "1"}
            )) @RequestParam(value = "type", required = false) String type
    ) {
        FanitPointResponse fanitPointResponse = fanitService.getMyPointHistory(account,integUid,type);
        return new GeneralResponse<>(fanitPointResponse);
    }
}
