package com.moyobab.server.place.controller;

import com.moyobab.server.global.exception.ApplicationException;
import com.moyobab.server.global.response.CommonResponse;
import com.moyobab.server.menucategory.entity.MenuCategoryType;
import com.moyobab.server.place.dto.PlaceResponseDto;
import com.moyobab.server.place.exception.PlaceErrorCase;
import com.moyobab.server.place.service.PlaceSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
@Validated
public class PlaceSearchController {

    private final PlaceSearchService placeSearchService;

    @GetMapping("/search")
    @Operation(
            summary = "음식점 검색",
            description = "카카오 API를 사용해 사용자가 선택한 카테고리의 주변 음식점을 검색합니다.",
            parameters = {
                    @Parameter(name = "category", description = "카테고리 타입", in = ParameterIn.QUERY, required = true, example = "CHICKEN"),
                    @Parameter(name = "x", description = "경도 (longitude)", in = ParameterIn.QUERY, required = true, example = "127.02758"),
                    @Parameter(name = "y", description = "위도 (latitude)", in = ParameterIn.QUERY, required = true, example = "37.49794"),
                    @Parameter(name = "radius", description = "검색 반경(m)", in = ParameterIn.QUERY, required = false, example = "2000")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (좌표 누락, 반경 범위 초과 등)",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "404", description = "해당 카테고리에 대한 결과 없음",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    public CommonResponse<List<PlaceResponseDto>> searchPlaces(
            @RequestParam(value = "category", required = false) MenuCategoryType category,
            @RequestParam(value = "x", required = false) Double x,
            @RequestParam(value = "y", required = false) Double y,
            @RequestParam(value = "radius", required = false, defaultValue = "2000") @Min(0) @Max(10000) Integer radius
    ) {
        if (category == null) {
            throw new ApplicationException(PlaceErrorCase.CATEGORY_REQUIRED);
        }
        if (x == null || y == null) {
            throw new ApplicationException(PlaceErrorCase.LOCATION_REQUIRED);
        }

        List<PlaceResponseDto> results = placeSearchService.searchByCategory(category, x, y, radius);

        if (results.isEmpty()) {
            throw new ApplicationException(PlaceErrorCase.PLACE_NOT_FOUND);
        }

        return CommonResponse.success(results);
    }
}

