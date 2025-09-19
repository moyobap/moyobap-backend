package com.moyobab.server.menucategory.controller;

import com.moyobab.server.global.response.CommonResponse;
import com.moyobab.server.menucategory.dto.MenuCategoryResponseDto;
import com.moyobab.server.menucategory.service.MenuCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menu-categories")
public class MenuCategoryController {

    private final MenuCategoryService menuCategoryService;

    @GetMapping
    @Operation(summary = "메뉴 카테고리 조회", description = "모든 메뉴 카테고리를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    public CommonResponse<List<MenuCategoryResponseDto>> getCategories() {
        return CommonResponse.success(menuCategoryService.getAllCategories());
    }
}
