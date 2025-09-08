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
}
