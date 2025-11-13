package com.moyobab.server.grouporder.controller;

import com.moyobab.server.global.exception.ApplicationException;
import com.moyobab.server.global.response.CommonResponse;
import com.moyobab.server.grouporder.dto.GroupOrderRequestDto;
import com.moyobab.server.grouporder.dto.GroupOrderResponseDto;
import com.moyobab.server.grouporder.service.GroupOrderService;
import com.moyobab.server.user.entity.User;
import com.moyobab.server.auth.resolver.CurrentUser;
import com.moyobab.server.user.exception.UserErrorCase;
import com.moyobab.server.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/group-orders")
@RequiredArgsConstructor
public class GroupOrderController {

    private final GroupOrderService groupOrderService;
    private final UserRepository userRepository;


    @PostMapping
    @Operation(
            summary = "그룹 생성",
            description = "사용자가 새로운 그룹을 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그룹 생성 성공",
                    content = @Content(schema = @Schema(implementation = GroupOrderResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 요청",
                    content = @Content)
    })
    public CommonResponse<GroupOrderResponseDto> createGroupOrder(
            @Parameter(hidden = true) @CurrentUser Long userId,
            @RequestBody @Valid GroupOrderRequestDto request
    ) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(UserErrorCase.USER_NOT_FOUND));
        GroupOrderResponseDto response = groupOrderService.createGroupOrder(request, creator);
        return CommonResponse.success(response);
    }

    @GetMapping("/active")
    @Operation(summary = "진행 중인 그룹 리스트 조회", description = "모집 중인 그룹들을 마감 시간 순으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "진행 중인 그룹 리스트 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = GroupOrderResponseDto.class))))
    })
    public CommonResponse<List<GroupOrderResponseDto>> getActiveGroupOrders() {
        List<GroupOrderResponseDto> results = groupOrderService.getActiveGroupOrders();
        return CommonResponse.success(results);
    }
}
