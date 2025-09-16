package com.moyobab.server.auth.service;

import com.moyobab.server.auth.dto.TokenResponseDto;
import com.moyobab.server.auth.exception.AuthErrorCase;
import com.moyobab.server.auth.mapper.AuthMapper;
import com.moyobab.server.auth.service.redis.RefreshTokenRedisService;
import com.moyobab.server.global.config.security.jwt.JwtTokenProvider;
import com.moyobab.server.global.exception.ApplicationException;
import com.moyobab.server.user.entity.LoginType;
import com.moyobab.server.user.entity.User;
import com.moyobab.server.user.exception.UserErrorCase;
import com.moyobab.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRedisService refreshTokenRedisService;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtProvider;

    // 로그인: 이메일 + 비밀번호 + 로그인 타입
    @Transactional
    public TokenResponseDto login(String email, String password, LoginType loginType) {
        User user = userRepository.findByEmailAndLoginType(email, loginType)
                .orElseThrow(() -> new ApplicationException(UserErrorCase.USER_NOT_FOUND));

        if (!encoder.matches(password, user.getPassword())) {
            throw new ApplicationException(AuthErrorCase.INVALID_PASSWORD);
        }

        String accessToken = jwtProvider.generateToken(user.getId(), "ROLE_USER");
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());
        Long expireAt = jwtProvider.getTokenExpiry(refreshToken);
        long ttl = expireAt - System.currentTimeMillis();
        if (ttl <= 0) throw new ApplicationException(AuthErrorCase.INVALID_REFRESH_TOKEN);

        refreshTokenRedisService.save(user.getId(), refreshToken, ttl);

        return AuthMapper.toTokenResponse(accessToken, refreshToken, user);
    }

    // Refresh Token을 통해 Access Token 재발급
    @Transactional
    public TokenResponseDto reissue(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new ApplicationException(AuthErrorCase.INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtProvider.getUserId(refreshToken);

        if (!refreshTokenRedisService.isSame(userId, refreshToken)) {
            throw new ApplicationException(AuthErrorCase.INVALID_REFRESH_TOKEN);
        }

        String newAccessToken = jwtProvider.generateToken(userId, "ROLE_USER");
        String newRefreshToken = jwtProvider.generateRefreshToken(userId);
        Long newExpireAt = jwtProvider.getTokenExpiry(newRefreshToken);
        long newTtl = newExpireAt - System.currentTimeMillis();
        if (newTtl <= 0) throw new ApplicationException(AuthErrorCase.INVALID_REFRESH_TOKEN);

        refreshTokenRedisService.save(userId, newRefreshToken, newTtl);

        return AuthMapper.toTokenResponse(newAccessToken, newRefreshToken);
    }

    // 로그아웃: Refresh Token 제거
    public void logout(Long userId) {
        refreshTokenRedisService.delete(userId);
    }
}
