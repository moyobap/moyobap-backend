package com.moyobab.server.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.moyobab.server.user.entity.LoginType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequestDto {
    @NotBlank
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Size(max = 320)
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 72)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "로그인 타입은 필수입니다.")
    private LoginType loginType;  // BASIC, KAKAO, NAVER
}
