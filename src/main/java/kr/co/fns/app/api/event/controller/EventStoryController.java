package kr.co.fns.app.api.event.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.fns.app.api.event.response.EventMessageResponse;
import kr.co.fns.app.api.event.response.EventResponse;
import kr.co.fns.app.api.event.service.EventService;
import kr.co.fns.app.api.fanit.model.response.FanitListResponse;
import kr.co.fns.app.api.fanit.service.FanitService;
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
@Tag(name = "EventStory API", description = "이벤트 댓글 관련 API 목록")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/event/{idx}/story")
@RestController
public class EventStoryController {

    private final EventService eventService;

    @GetMapping("")
    @Operation(summary = "대댓글 이벤트 댓글 리스트 API", description="")
    public GeneralResponse<EventResponse> getEventStoryList(
            Locale locale,
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(value = "page", required = false) int page,
            @Parameter(description = "event 번호", example = "10002") @PathVariable(name = "idx", required = true) int eventId
    ) {
        EventResponse eventResponse = eventService.getEventStoryList(page,eventId);

        return new GeneralResponse<>(eventResponse);
    }

    @PostMapping("")
    @Operation(summary = "대댓글 이벤트 댓글 등록 API (access_token 필수)", description="")
    public GeneralResponse<EventMessageResponse> saveEventStory(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "event 번호", example = "10002") @PathVariable(name = "idx", required = true) int eventId,
            @Parameter(description = "통합정보 UID", example = "ft_u_0096359625c9863edea1f11edb390ad3abdb0cf95") @RequestParam(value = "integUid", required = true) String integUid,
            @Parameter(description = "comment", example = "펜투 재밋어요~") @RequestParam(value = "comment", required = false) String comment
    ) {
        EventMessageResponse eventMessageResponse = eventService.saveEventStory(account,eventId,integUid,comment);

        return new GeneralResponse<>(eventMessageResponse);
    }

    @DeleteMapping("/{storyIdx}")
    @Operation(summary = "대댓글 이벤트 댓글 삭제 API (access_token 필수)", description="")
    public GeneralResponse<EventMessageResponse> deleteEventStory(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "event 번호", example = "10002") @PathVariable(name = "idx", required = true) int eventId,
            @Parameter(description = "삭제할 story 번호 PK", example = "1") @PathVariable(value = "storyIdx", required = true) int storyId,
            @Parameter(description = "통합정보 UID", example = "ft_u_0096359625c9863edea1f11edb390ad3abdb0cf95") @RequestParam(value = "integUid", required = true) String integUid
    ) {
        EventMessageResponse eventMessageResponse = eventService.deleteEventStory(account,integUid,storyId,eventId);

        return new GeneralResponse<>(eventMessageResponse);
    }
}
