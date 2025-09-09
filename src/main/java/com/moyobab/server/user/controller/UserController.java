package com.moyobab.server.user.controller;

import com.moyobab.server.global.exception.ApplicationException;
import com.moyobab.server.global.response.CommonResponse;
import com.moyobab.server.user.dto.EmailCheckResponseDto;
import com.moyobab.server.user.dto.NicknameCheckResponseDto;
import com.moyobab.server.user.dto.UserSignUpRequestDto;
import com.moyobab.server.user.exception.UserErrorCase;
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
    @Operation(summary = "이메일 중복 확인", description = "유저가 입력한 이메일이 이미 사용 중인지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "중복 여부 반환",
                    content = @Content(schema = @Schema(implementation = EmailCheckResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "파라미터 누락 등 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "409", description = "이메일 중복",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    public CommonResponse<EmailCheckResponseDto> checkEmail(@RequestParam(value = "email", required = false) String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ApplicationException(UserErrorCase.EMAIL_REQUIRED);
        }
        if (!userService.isEmailAvailable(email.trim())) {
            throw new ApplicationException(UserErrorCase.EMAIL_DUPLICATED);
        }
        return CommonResponse.success(new EmailCheckResponseDto(true, "사용 가능한 이메일입니다."));
    }

    @GetMapping("/check-nickname")
    @Operation(summary = "닉네임 중복 확인", description = "유저가 등록하려는 닉네임이 이미 사용 중인지 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "중복 여부 반환",
                    content = @Content(schema = @Schema(implementation = NicknameCheckResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "파라미터 누락 등 잘못된 요청",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "409", description = "닉네임 중복",
                    content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    })
    public CommonResponse<NicknameCheckResponseDto> checkNickname(@RequestParam(value = "nickname", required = false) String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new ApplicationException(UserErrorCase.NICKNAME_REQUIRED);
        }
        if (!userService.isNicknameAvailable(nickname.trim())) {
            throw new ApplicationException(UserErrorCase.NICKNAME_DUPLICATED);
        }
        return CommonResponse.success(new NicknameCheckResponseDto(true, "사용 가능한 닉네임입니다."));
    }

}
