package kr.co.fns.app.api.fanit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.fns.app.api.fanit.model.response.FanitMessageResponse;
import kr.co.fns.app.api.fanit.model.response.FanitStoryResponse;
import kr.co.fns.app.api.fanit.service.FanitStoryService;
import kr.co.fns.app.base.dto.GeneralResponse;
import kr.co.fns.app.config.resolver.Account;
import kr.co.fns.app.config.resolver.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@ApiResponses({
        @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = GeneralResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘 못된 접근"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
})
@Tag(name = "Fanit Story API", description = "팬잇 투표 댓글 API 목록")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/fanit/{idx}/story")
@RestController
public class FanitStoryController {

    private final FanitStoryService fanitStoryService;

    @GetMapping("")
    @Operation(summary = "팬잇 투표 댓글 리스트 API", description="")
    public GeneralResponse<FanitStoryResponse> getList(
            Locale locale,
            @Parameter(description = "fanit 번호", example = "6") @PathVariable(name = "idx", required = true) int fanitId,
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(value = "page", required = false) int page
    ) {
        FanitStoryResponse fanitStoryResponse = fanitStoryService.getFanitStoryList(page,fanitId);
        return new GeneralResponse<>(fanitStoryResponse);
    }

    @PostMapping("")
    @Operation(summary = "팬잇 투표 댓글 입력 API (access_token 필수)", description="")
    public GeneralResponse<FanitMessageResponse> saveFanitStory(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "fanit 번호", example = "6") @PathVariable(value = "idx", required = true) int fanitId,
            @Parameter(description = "통합정보 UID", example = "ft_u_0096359625c9863edea1f11edb390ad3abdb0cf95") @RequestParam(value = "integUid", required = true) String integUid,
            @Parameter(description = "comment", example = "펜투 재밋어요~") @RequestParam(value = "comment", required = true) String comment
    ) {
        FanitMessageResponse fanitMessageResponse = fanitStoryService.saveFanitStory(account,integUid,fanitId,comment);
        return new GeneralResponse<>(fanitMessageResponse);
    }

    @DeleteMapping("/{storyIdx}")
    @Operation(summary = "팬잇 투표 댓글 삭제 API (access_token 필수)", description="")
    public GeneralResponse<FanitMessageResponse> deleteFanitStory(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "fanit 번호", example = "6") @PathVariable(value = "idx", required = true) int fanitId,
            @Parameter(description = "삭제할 story 번호 PK", example = "11796") @PathVariable(name = "storyIdx", required = true) int storyId,
            @Parameter(description = "통합정보 UID", example = "ft_u_0096359625c9863edea1f11edb390ad3abdb0cf95") @RequestParam(value = "integUid", required = true) String integUid
    ) {
        FanitMessageResponse fanitMessageResponse = fanitStoryService.deleteFanitStory(account,integUid,storyId,fanitId);
        return new GeneralResponse<>(fanitMessageResponse);
    }

}
