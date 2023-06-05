package kr.co.fns.app.api.minute.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.fns.app.api.minute.model.request.MinuteInsertRequest;
import kr.co.fns.app.api.minute.model.request.MinuteUpdateRequest;
import kr.co.fns.app.api.minute.model.response.MinuteDetailResponse;
import kr.co.fns.app.api.minute.model.response.MinuteLastResponse;
import kr.co.fns.app.api.minute.model.response.MinuteListResponse;
import kr.co.fns.app.api.minute.model.response.MinuteSpecificListResponse;
import kr.co.fns.app.api.minute.service.MinuteService;
import kr.co.fns.app.base.dto.CommonResponse;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigInteger;
import java.util.Locale;

@Tag(name = "Minute API", description = "미닛 관련 API 목록")
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/minute")
@RestController
public class MinuteController {

    private final MinuteService minuteService;

    @Operation(summary = "minute, 미닛 상세 데이터 출력", description="미닛 상세 데이터 출력 (access_token 선택), 통합 UID 선택적 파라미터")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = MinuteDetailResponse.class)))
    })
    @GetMapping("/{idx}")
    public GeneralResponse<MinuteDetailResponse> minuteDetail(
            Locale locale,
            @Parameter(description = "Minute 번호", example = "45") @PathVariable(name = "idx") BigInteger idx,
            @Parameter(description = "통합정보 UID", example = "xx_u_2593789567d2f5d2adf5a11eda6f27538ce5e4dxx") @RequestParam(value = "integUid", required = false) String integUid
    ) {
        MinuteDetailResponse minuteDetailResponse = minuteService.getMinuteDetail(idx, integUid);

        return new GeneralResponse<>(minuteDetailResponse);
    }

    @Operation(summary = "minute, 미닛 게시글 제한 체크(동일 사용자가 바로 여려 미닛 등록 못하게 함)", description="게시판 리스트 마지막에 올라온 minuteWriteLimit 숫자에 따라서 게시글 제한을 해준다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = MinuteLastResponse.class)))
    })
    @GetMapping("/last")
    public GeneralResponse<MinuteLastResponse> minuteLast()
    {
        MinuteLastResponse minuteLastResponse = minuteService.getLastMinute();

        return new GeneralResponse<>(minuteLastResponse);
    }

    @Operation(summary = "minute, 미닛의 마지막 댓글 등록한 사용자 UID 리턴", description="Minute {idx}의 마지막 댓글 작성한 사용자를 리턴한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = MinuteLastResponse.class)))
    })
    @GetMapping("/{idx}/reply-last")
    public GeneralResponse<MinuteLastResponse> minuteReplyLast(
            Locale locale,
            @Parameter(description = "Minute 번호", example = "74") @PathVariable(name = "idx") BigInteger idx
    ) {
        MinuteLastResponse minuteLastResponse = minuteService.getMinuteReplyLast(idx);

        return new GeneralResponse<>(minuteLastResponse);
    }

    @Operation(summary = "minute, 미닛 뷰 카운트 증가", description = "미닛 뷰 카운트를 증가한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    @PutMapping("/{idx}/view-count")
    public GeneralResponse<CommonResponse> minuteViewCountUpdate(
            Locale locale,
            @Parameter(description = "Minute 번호", example = "45") @PathVariable(name = "idx") BigInteger idx
    ) {
        int cnt = minuteService.setMinuteViewCountUpdate(idx);

        return new GeneralResponse<>(CommonResponse.builder().result(cnt).build());
    }

    @Operation(summary = "minute, 미닛 리스트 조회", description = "미닛 리스트를 조회한다.<br>" +
            "Sorting 구분 <br>" +
            " - 1 :  조회수 + (좋아요*3) + 댓글수 <br>" +
            " - 2 : ((좋아요*3) - 싫어요) + 댓글수*2<br>" +
            " - 3 : 최신순 <br>" +
            " - 4 : 선호순 <br>" +
            " - 5 : 댓글수 <br>" +
            " - 6 : 조회수<br>" +
            " - 7 : 내 작성글 보기 <br>" +
            " - 8 : 내 답글 보기 <br>" +
            " - 9 : 순번 (ASC) <br>" +
            "해시태그검색 : 해시태그(==), 타이틀,본문(like)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = MinuteListResponse.class)))
    })
    @GetMapping("")
    public GeneralResponse<MinuteListResponse> minuteList(
            Locale locale,
            @Parameter(description = "통합정보 UID", example = "xx_u_2593789567d2f5d2adf5a11eda6f27538ce5e4dxx") @RequestParam(value = "integUid", required = false) String integUid,
            @Parameter(description = "페이지번호", example = "1") @RequestParam(value = "page", required = true) int page,
            @Parameter(description = "페이징 처리여부", example = "false") @RequestParam(value = "nextCheck", required = false) String nextCheck,
            @Parameter(description = "Sorting 구분", example = "1") @RequestParam(value = "sortingNum", required = true) @Min(1) @Max(8) int sortingNum,
            @Parameter(description = "해시태그검색 ", example = "맨발의 청춘") @RequestParam(value = "searchText", required = false) String searchText
    ) {
        boolean isNextCheck = false;
        if (!StringUtils.isEmpty(nextCheck)) {
             isNextCheck = Boolean.parseBoolean(nextCheck);
        }
        if (StringUtils.isEmpty(integUid)) integUid = "";
        if (StringUtils.isEmpty(searchText)) searchText = "";

        MinuteListResponse resultLst = minuteService.getList(integUid, page, isNextCheck, sortingNum, searchText);

        return new GeneralResponse<>(resultLst);
    }

    @Operation(summary = "minute, 특정유저가 올린 미닛리스트 제공 API", description = "토큰Check가 패스인 경우는 토큰값이 있으면 해당 토큰값을 통해 본인의 integ_uid를 가져와서 해당 미닛리스트들에 대해 좋아요 및 <br>" +
            "차단여부등을 확인하여 적용하기 위해 사용중에 있음 토큰값이 없어도 호출 가능함 <br>" +
            "검색단어 및 검색추가옵션(=total)이 있는 경우 : 제목과 내용으로 검색 <br> " +
            "검색단어만 있는 경우 : 사용자 NickName (= 검색) <br>" +
            "Sorting 구분 <br> " +
            " - 1 : 조회수 + (좋아요*3) + 댓글수 <br>" +
            " - 2 : ((좋아요*3) - 싫어요) + 댓글수*2 <br>" +
            " - 3 : 최신순 <br>" +
            " - 4 : 좋아요 - 싫어요<br>" +
            " - 5 : 댓글순 <br>" +
            " - 6 : 조회순 <br>" +
            " - 9 : 순번 (ASC) <br>"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = MinuteListResponse.class)))
    })

    @GetMapping("/specific-persion-list")
    public GeneralResponse<MinuteSpecificListResponse> minuteSpecificList(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "통합정보 UID", example = "통합정보 UID") @RequestParam(value = "integUid", required = true) String integUid,
            @Parameter(description = "페이지번호", example = "1") @RequestParam(value = "page", required = true) int page,
            @Parameter(description = "페이징 처리여부", example = "false") @RequestParam(value = "nextCheck", required = false) String nextCheck,
            @Parameter(description = "Sorting 구분", example = "1") @RequestParam(value = "sortingNum", required = true) @Min(1) @Max(9) int sortingNum,
            @Parameter(description = "검색단어 ", example = "클럽") @RequestParam(value = "searchText", required = false) String searchText,
            @Parameter(description = "검색추가옵션 ", example = "total") @RequestParam(value = "classifiValue", required = false) String classifiValue
    ) {
        boolean isNextCheck = false;
        if (!StringUtils.isEmpty(nextCheck)) {
            isNextCheck = Boolean.parseBoolean(nextCheck);
        }
        if (StringUtils.isEmpty(searchText)) searchText = "";   // 검색단어
        if (StringUtils.isEmpty(classifiValue)) classifiValue = ""; // 검색 옵션
        log.debug("account info = {}, {}, {}, {}", account.getIntegUid(), account.getLoginType(), account.getUserIp(), account.getTokenInfo());

        MinuteSpecificListResponse resultLst = minuteService.getSpecificList(integUid, account, page, isNextCheck, sortingNum, searchText, classifiValue);

        return new GeneralResponse<>(resultLst);
    }

    @Operation(summary = "minute, 신규 등록 API", description = "미닛 신규 등록한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    @PostMapping("")
    public GeneralResponse<CommonResponse> minuteWrite(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "미닛 등록") @Valid @RequestBody MinuteInsertRequest minuteRequest
    ) {
        log.debug(account.getIntegUid());
        log.debug("minuteRequest is null => {}", minuteRequest == null ? true : false);
        log.debug("minuteRequest => {}, {}, {}", minuteRequest.getLang(), minuteRequest.getContent(), minuteRequest.getTitle());

        if (!account.getIntegUid().equals(minuteRequest.getIntegUid())) {
            throw new FantooException(ErrorCode.AccessDeniedException);
        }
        String result = minuteService.insertMinute(account, minuteRequest);

        return new GeneralResponse<>(CommonResponse.builder().result(result).build());
    }

    @Operation(summary = "minute, 미닛 수정 API", description = "등록된 미닛 데이터를 수정한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/{idx}")
    public GeneralResponse<String> minuteModify(
            Locale locale,
            @Parameter(description = "통합정보 UID(Token)", example = "통합정보 UID") @RequestParam(value = "integUid", required = true) String integUid,
            @Parameter(description = "Minute 번호", example = "867") @PathVariable(name = "idx") BigInteger idx,
            @Parameter(description = "미닛 수정") @Valid @RequestBody MinuteUpdateRequest minuteRequest
    ) {
        String result = minuteService.updateMinute(integUid, idx, minuteRequest);

        return new GeneralResponse<>(result);
    }

    @Operation(summary = "minute, 미닛 삭제 API", description = "미닛을 삭제한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/{idx}")
    public GeneralResponse<String> minuteDelete(
            Locale locale,
            @Parameter(description = "통합정보 UID(Token)", example = "통합정보 UID") @RequestParam(value = "integUid", required = true) String integUid,
            @Parameter(description = "Minute 번호", example = "867") @PathVariable(name = "idx") BigInteger idx
    ) {
        String result = minuteService.deleteMinute(idx, integUid);

        return new GeneralResponse<>(result);
    }


    @Operation(summary = "minute, 미닛 등록 여부 확인", description = "해당 User가 미닛에 등록 가능한지 체크한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/write-check")
    public GeneralResponse<String> isMinuteWritable(
            Locale locale,
            @Parameter(description = "통합정보 UID", example = "xx_u_2593789567d2f5d2adf5a11eda6f27538ce5e4dxx") @RequestParam(value = "integUid", required = true) String integUid
    ) {
        String result = minuteService.isMinuteWritable(integUid);

        return new GeneralResponse<>(result);
    }








//    @PostMapping("minute/report/{targetId}")
//    @ApiOperation(value = "minute, 게시글 / 댓글 신고 insert | (access_token필수)", notes="커뮤니티, 게시글 / 댓글 신고 insert <br/><br/>" +
//            "* 해당 API는 minute 게시글 / 댓글에 대해 1건씩 신고 내용을 저장합니다. <br/>" +
//            "* reportMessageId 값은 신고 메세지의 신고 사유에 해당하는 pk id 값 입니다. <br/>" +
//            "* 똑같은 게시글을 중복 신고시 'FE2025 : 이미 신고한 글 입니다' 라는 에러메세지가 내려갑니다. 에러메세지 내용은 변경 될 수 있습니다. <br/>"
//    )
//    public void minuteReportInsert(Locale locale
//            , @ApiParam(value = "통합정보 UID",example = "", hidden = true) @RequestParam(value = "integUid",required = true ) String integUid
//            , @ApiParam(value = "Target Type",example = "") @RequestParam(name = "targetType") TargetType targetType
//            , @ApiParam(value = "Target ID, 게시글 or 댓글의 postId, replyId",example = "") @PathVariable(name = "targetId") int targetId
//            , @ApiParam(value = "커뮤니티 신고 대상") @Valid @RequestBody ComReportDto comReportDto) {
//
//        ReportDto.ReportReq req = new ReportDto.ReportReq();
//        req.setIntegUid(integUid);
//        req.setTargetIntegUid(comReportDto.getTargetIntegUid());
//        req.setTargetId(targetId);
//        req.setTargetType(targetType);
//        req.setReportMessageId(comReportDto.getReportMessageId());
//        req.setServiceType(ServiceType.MINUTE);
//
//        reportService.setReport(req);
//    }
}
