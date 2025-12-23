package com.moyobab.server.participant.controller;

import com.moyobab.server.auth.resolver.CurrentUser;
import com.moyobab.server.global.response.CommonResponse;
import com.moyobab.server.participant.dto.ParticipantJoinRequestDto;
import com.moyobab.server.participant.dto.ParticipantUpdateAmountRequestDto;
import com.moyobab.server.participant.service.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group-orders")
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/{groupOrderId}/participants")
    @Operation(summary = "그룹 주문 참여", description = "특정 그룹 주문에 참여하고 주문 금액을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "그룹 참여 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (중복 참여, 모집 종료, 금액 오류 등)",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 요청",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "그룹 주문 또는 사용자 정보 없음",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))
            )
    })
    public CommonResponse<String> joinGroup(
            @Parameter(description = "그룹 주문 ID", required = true)
            @PathVariable("groupOrderId") Long groupOrderId,
            @Parameter(hidden = true) @CurrentUser Long userId,
            @RequestBody @Valid ParticipantJoinRequestDto request
    ) {
        participantService.joinGroup(groupOrderId, userId, request);
        return CommonResponse.success("그룹 참여 완료");
    }

    @PatchMapping("/{groupOrderId}/participants/me")
    @Operation(summary = "내 참여 금액 수정", description = "그룹 주문에서 내가 등록한 주문 금액을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "금액 수정 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (모집 종료, 금액 오류 등)",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 요청",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "404", description = "참여 내역 또는 그룹 주문 없음",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    public CommonResponse<String> updateMyAmount(
            @Parameter(description = "그룹 주문 ID", required = true)
            @PathVariable("groupOrderId") Long groupOrderId,
            @Parameter(hidden = true) @CurrentUser Long userId,
            @RequestBody @Valid ParticipantUpdateAmountRequestDto request
    ) {
        participantService.updateMyOrderAmount(groupOrderId, userId, request);
        return CommonResponse.success("주문 금액 수정 완료");
    }
}