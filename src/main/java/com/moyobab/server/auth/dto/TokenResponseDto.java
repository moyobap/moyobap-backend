package com.moyobab.server.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
