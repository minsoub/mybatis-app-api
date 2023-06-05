package kr.co.fns.app.api.minute.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.fns.app.api.minute.model.enums.FavoriteType;
import kr.co.fns.app.api.minute.model.enums.LikeType;
import kr.co.fns.app.api.minute.model.enums.SubType;
import kr.co.fns.app.api.minute.model.response.*;
import kr.co.fns.app.api.minute.service.MinuteFavoriteService;
import kr.co.fns.app.api.minute.service.MinuteLikeService;
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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigInteger;
import java.util.Locale;

@Tag(name = "Minute 즐겨찾기 API", description = "미닛 즐겨찾기 API 목록")
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/minute")
@RestController
public class MinuteFavoriteController {

    private final MinuteFavoriteService minuteFavoriteService;

    @Operation(summary = "minute, 미닛 즐겨찾기 설정 API", description = "나의 등록된 미닛의 즐겨찾기 설정한다. <br>" +
            "Favorite Type : fav(즐겨찾기 추가), fav_cancel(즐겨찾기 취소)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = MinuteFavoriteResponse.class)))
    })
    @PutMapping("/{idx}/favorite")
    public GeneralResponse<MinuteFavoriteResponse> minuteFavorite(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "통합정보 UID", example = "통합정보 UID") @RequestParam(value = "integUid") String integUid,
            @Parameter(description = "Minute idx", example = "60") @PathVariable(name = "idx") BigInteger idx,
            @Parameter(description = "Favorite Type", example = "fav") @RequestParam(value = "favType") FavoriteType favType
    ) {
        if (!account.getIntegUid().equals(integUid)) {
            throw new FantooException(ErrorCode.AccessDeniedException);
        }
        MinuteFavoriteResponse result = minuteFavoriteService.setMinuteFavorite(account.getIntegUid(), idx, favType);

        return new GeneralResponse<>(result);
    }

    @Operation(summary = "minute, 미닛 즐겨찾기 Check API", description = "나의 등록된 미닛의 즐겨찾기 Check.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{idx}/favorite")
    public GeneralResponse<MinuteFavoriteDetailResponse> isMinuteFavorite(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "통합정보 UID", example = "통합정보 UID") @RequestParam(value = "integUid") String integUid,
            @Parameter(description = "Minute idx", example = "60") @PathVariable(name = "idx") BigInteger idx
    ) {
        if (!account.getIntegUid().equals(integUid)) {
            throw new FantooException(ErrorCode.AccessDeniedException);
        }
        MinuteFavoriteDetailResponse result = minuteFavoriteService.currentMinuteFavorite(account.getIntegUid(), idx);

        return new GeneralResponse<>(result);
    }

    @Operation(summary = "minute, 미닛 Favorites 리스트 조회", description = "나의 미닛 Favorites 리스트를 조회한다. <br>" +
            "Sorting 구분 : 9 (ASC) Others (DESC)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = MinuteFavoritesListResponse.class)))
    })
    @GetMapping("/favorite")
    public GeneralResponse<MinuteFavoritesListResponse> minuteList(
            Locale locale,
            @Parameter(hidden = true) @CurrentUser Account account,
            @Parameter(description = "통합정보 UID", example = "xx_u_2593789567d2f5d2adf5a11eda6f27538ce5e4dxx") @RequestParam(value = "integUid") String integUid,
            @Parameter(description = "페이지번호", example = "1") @RequestParam(value = "page", required = true) int page,
            @Parameter(description = "페이징 처리여부", example = "false") @RequestParam(value = "nextCheck", required = false, defaultValue = "false") boolean nextCheck,
            @Parameter(description = "Sorting 구분", example = "9") @RequestParam(value = "sortingNum", required = false, defaultValue = "9")  int sortingNum
    ) {
        if (!account.getIntegUid().equals(integUid)) {
            throw new FantooException(ErrorCode.AccessDeniedException);
        }
        // sortingNum의 경우 최신순(3)과 역순(9)만 사용함.
        MinuteFavoritesListResponse resultLst = minuteFavoriteService.getFavoritesList(account.getIntegUid(), page, nextCheck, sortingNum);

        return new GeneralResponse<>(resultLst);
    }
}
