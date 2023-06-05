package kr.co.fns.app.api.event.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.fns.app.api.event.response.EventMessageResponse;
import kr.co.fns.app.api.event.response.EventReplyResponse;
import kr.co.fns.app.api.event.response.EventResponse;
import kr.co.fns.app.api.event.service.EventReplyService;
import kr.co.fns.app.api.event.service.EventService;
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
@Tag(name = "EventStoryReply API", description = "이벤트 대댓글 관련 API 목록")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/event/story/{idx}/reply")
@RestController
public class EventStoryReplyController {

    private final EventReplyService eventReplyService;

    @GetMapping("")
    @Operation(summary = "대댓글 이벤트 대댓글 리스트 API", description="")
    public GeneralResponse<EventReplyResponse> getEventStoryList(
            Locale locale,
            @Parameter(description = "story 번호", example = "1530") @PathVariable(value = "idx", required = true) int storyId,
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(value = "page", required = false) int page
    ) {
        EventReplyResponse eventReplyResponse = eventReplyService.getEventStoryReplyList(page,storyId);

        return new GeneralResponse<>(eventReplyResponse);
    }

    @PostMapping("")
    @Operation(summary = "대댓글 이벤트 대댓글 등록 API (access_token 필수)", description="")
    public GeneralResponse<EventMessageResponse> saveEventStory(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "story 번호", example = "1530") @PathVariable(value = "idx", required = true) int storyId,
            @Parameter(description = "통합정보 UID", example = "ft_u_0096359625c9863edea1f11edb390ad3abdb0cf95") @RequestParam(value = "integUid", required = true) String integUid,
            @Parameter(description = "comment", example = "펜투 재밋어요~") @RequestParam(value = "comment", required = false) String comment
    ) {
        EventMessageResponse eventMessageResponse = eventReplyService.saveEventStoryReply(account,storyId,integUid,comment);

        return new GeneralResponse<>(eventMessageResponse);
    }

    @DeleteMapping("/{storyReplyIdx}")
    @Operation(summary = "대댓글 이벤트 대댓글  삭제 API (access_token 필수)", description="")
    public GeneralResponse<EventMessageResponse> deleteEventStory(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "story 번호", example = "1530") @PathVariable(value = "idx", required = true) int storyId,
            @Parameter(description = "삭제할 대댓글 번호 PK", example = "208969") @PathVariable(value = "storyReplyIdx", required = true) int replyStoryId,
            @Parameter(description = "통합정보 UID", example = "ft_u_0096359625c9863edea1f11edb390ad3abdb0cf95") @RequestParam(value = "integUid", required = true) String integUid
    ) {
        EventMessageResponse eventMessageResponse = eventReplyService.deleteEventStoryReply(account,integUid,replyStoryId,storyId);

        return new GeneralResponse<>(eventMessageResponse);
    }
}
