package com.moyobab.server.auth.dto;

import com.moyobab.server.user.dto.UserResponseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
    private UserResponseDto user;
}
