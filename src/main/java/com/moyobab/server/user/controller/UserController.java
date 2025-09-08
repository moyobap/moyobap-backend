package com.moyobab.server.user.controller;

import com.moyobab.server.global.response.CommonResponse;
import com.moyobab.server.user.dto.UserSignUpRequestDto;
import com.moyobab.server.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "유저 회원가입", description = "신규 유저 회원가입을 수행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 회원",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    public CommonResponse<String> signup(@RequestBody @Valid UserSignUpRequestDto req) {
        userService.signup(req);
        return CommonResponse.success("회원가입 성공");
    }

    @GetMapping("/check-email")
    @Operation(summary = "이메일 중복 확인", description = "이미 가입된 이메일인지 확인합니다.")
    public CommonResponse<Boolean> checkEmail(@RequestParam String email) {
        boolean duplicated = userService.isEmailDuplicated(email);
        return CommonResponse.success(!duplicated); // 중복 X → true
    }

    @GetMapping("/check-nickname")
    @Operation(summary = "닉네임 중복 확인", description = "이미 사용 중인 닉네임인지 확인합니다.")
    public CommonResponse<Boolean> checkNickname(@RequestParam String nickname) {
        boolean duplicated = userService.isNicknameDuplicated(nickname);
        return CommonResponse.success(!duplicated); // 중복 X → true
    }
}
