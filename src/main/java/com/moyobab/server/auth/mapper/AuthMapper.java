package com.moyobab.server.auth.mapper;

import com.moyobab.server.auth.dto.TokenResponseDto;
import com.moyobab.server.auth.entity.RefreshToken;

public class AuthMapper {

    // Entity에서 DTO
    public static TokenResponseDto toTokenResponse(String accessToken, String refreshToken) {
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // DTO에서 Entity
    public static RefreshToken toRefreshToken(Long userId, String token, Long expiry) {
        return RefreshToken.builder()
                .userId(userId)
                .token(token)
                .expiry(expiry)
                .build();
    }
}
