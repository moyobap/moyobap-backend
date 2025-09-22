package com.moyobab.server.place.controller;

import com.moyobab.server.global.response.CommonResponse;
import com.moyobab.server.menucategory.entity.MenuCategoryType;
import com.moyobab.server.place.dto.PlaceResponseDto;
import com.moyobab.server.place.service.PlaceSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
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
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    array = @ArraySchema(schema = @Schema(implementation = PlaceResponseDto.class))
                            )
                    )
            }
    )
    public CommonResponse<List<PlaceResponseDto>> searchPlaces(
            @RequestParam("category") MenuCategoryType category,
            @RequestParam("x") Double x,
            @RequestParam("y") Double y,
            @RequestParam(value = "radius", required = false, defaultValue = "2000") Integer radius
    ) {
        List<PlaceResponseDto> results = placeSearchService.searchByCategory(category, x, y, radius);
        return CommonResponse.success(results);
    }
}

