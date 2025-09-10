package com.moyobab.server.auth.controller;

import com.moyobab.server.auth.dto.TokenResponseDto;
import com.moyobab.server.user.dto.UserLoginRequestDto;
import com.moyobab.server.auth.exception.AuthErrorCase;
import com.moyobab.server.auth.service.AuthService;
import com.moyobab.server.global.exception.ApplicationException;
import com.moyobab.server.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일, 비밀번호, 로그인 타입을 이용해 로그인 후 토큰을 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = TokenResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "비밀번호 불일치", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자 없음", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    public CommonResponse<TokenResponseDto> login(@RequestBody @Valid UserLoginRequestDto request) {
        TokenResponseDto tokens = authService.login(
                request.getEmail(),
                request.getPassword(),
                request.getLoginType()
        );
        return CommonResponse.success(tokens);
    }

    @PostMapping("/reissue")
    @Operation(summary = "Access Token 재발급", description = "유효한 Refresh Token을 이용해 Access Token을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재발급 성공", content = @Content(schema = @Schema(implementation = TokenResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "리프레시 토큰 오류", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    public CommonResponse<TokenResponseDto> reissue(@RequestParam("refreshToken") String refreshToken) {
        TokenResponseDto newTokens = authService.reissue(refreshToken);
        return CommonResponse.success(newTokens);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "현재 유저의 Refresh Token을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 정보 없음", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    public CommonResponse<String> logout(@RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new ApplicationException(AuthErrorCase.UNAUTHORIZED);
        }

        String token = accessToken.substring(7);
        if (token.isEmpty()) {
            throw new ApplicationException(AuthErrorCase.UNAUTHORIZED);
        }

        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authService.logout(userId);
        return CommonResponse.success("로그아웃 되었습니다.");
    }
}
