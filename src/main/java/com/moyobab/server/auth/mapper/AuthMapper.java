package com.moyobab.server.auth.mapper;

import com.moyobab.server.auth.dto.TokenResponse;
import com.moyobab.server.auth.entity.RefreshToken;

public class AuthMapper {

    // Entity에서 DTO
    public static TokenResponse toTokenResponse(String accessToken, String refreshToken) {
        return TokenResponse.builder()
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
