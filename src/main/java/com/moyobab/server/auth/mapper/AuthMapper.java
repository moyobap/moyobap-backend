package com.moyobab.server.auth.mapper;

import com.moyobab.server.auth.dto.TokenResponseDto;
import com.moyobab.server.auth.entity.RefreshToken;
import com.moyobab.server.user.dto.UserResponseDto;
import com.moyobab.server.user.entity.User;

public class AuthMapper {

    public static TokenResponseDto toTokenResponse(String accessToken, String refreshToken, User user) {
        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(UserResponseDto.builder()
                        .userId(user.getId())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .build())
                .build();
    }


    // 리프레시 재발급용
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
