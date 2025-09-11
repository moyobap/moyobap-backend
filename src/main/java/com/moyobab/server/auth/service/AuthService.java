package com.moyobab.server.auth.service;

import com.moyobab.server.auth.dto.TokenResponseDto;
import com.moyobab.server.auth.entity.RefreshToken;
import com.moyobab.server.auth.exception.AuthErrorCase;
import com.moyobab.server.auth.mapper.AuthMapper;
import com.moyobab.server.auth.repository.RefreshTokenRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtProvider;

    // 로그인: 이메일 + 비밀번호 + 로그인 타입
    public TokenResponseDto login(String email, String password, LoginType loginType) {
        User user = userRepository.findByEmailAndLoginType(email, loginType)
                .orElseThrow(() -> new ApplicationException(UserErrorCase.USER_NOT_FOUND));

        if (!encoder.matches(password, user.getPassword())) {
            throw new ApplicationException(AuthErrorCase.INVALID_PASSWORD);
        }

        String accessToken = jwtProvider.generateToken(user.getId(), "ROLE_USER");
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());
        Long expiry = jwtProvider.getTokenExpiry(refreshToken);

        refreshTokenRepository.deleteByUserId(user.getId());
        RefreshToken newToken = AuthMapper.toRefreshToken(user.getId(), refreshToken, expiry);
        refreshTokenRepository.save(newToken);

        return AuthMapper.toTokenResponse(accessToken, refreshToken);
    }

    // Refresh Token을 통해 Access Token 재발급
    public TokenResponseDto reissue(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new ApplicationException(AuthErrorCase.INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtProvider.getUserId(refreshToken);
        RefreshToken saved = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new ApplicationException(AuthErrorCase.INVALID_REFRESH_TOKEN));

        if (!saved.getToken().equals(refreshToken)) {
            throw new ApplicationException(AuthErrorCase.INVALID_REFRESH_TOKEN);
        }

        if (saved.getExpiry() < System.currentTimeMillis()) {
            refreshTokenRepository.delete(saved);
            throw new ApplicationException(AuthErrorCase.EXPIRED_REFRESH_TOKEN);
        }

        String newAccessToken = jwtProvider.generateToken(userId, "ROLE_USER");
        String newRefreshToken = jwtProvider.generateRefreshToken(userId);
        Long newExpiry = jwtProvider.getTokenExpiry(newRefreshToken);

        saved.setToken(newRefreshToken);
        saved.setExpiry(newExpiry);
        refreshTokenRepository.save(saved);

        return AuthMapper.toTokenResponse(newAccessToken, newRefreshToken);
    }

    // 로그아웃: Refresh Token 제거
    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
