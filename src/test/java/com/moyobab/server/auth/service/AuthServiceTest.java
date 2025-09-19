package com.moyobab.server.auth.service;

import com.moyobab.server.auth.dto.TokenResponseDto;
import com.moyobab.server.auth.exception.AuthErrorCase;
import com.moyobab.server.auth.service.redis.RefreshTokenRedisService;
import com.moyobab.server.global.config.security.jwt.JwtTokenProvider;
import com.moyobab.server.global.exception.ApplicationException;
import com.moyobab.server.user.entity.LoginType;
import com.moyobab.server.user.entity.User;
import com.moyobab.server.user.exception.UserErrorCase;
import com.moyobab.server.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenRedisService redisService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    private final Long userId = 1L;
    private final String email = "test1@naver.com";
    private final String password = "test!1234";
    private final String encodedPassword = "encodedPassword";
    private final String accessToken = "access.token";
    private final String refreshToken = "refresh.token";
    private final Long futureTimestamp = System.currentTimeMillis() + 100000;

    private User mockUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        mockUser = User.builder()
                .id(userId)
                .email(email)
                .password(encodedPassword)
                .nickname("nickname")
                .username("username")
                .loginType(LoginType.BASIC)
                .birthDate(LocalDate.of(2000, 1, 1))
                .build();
    }

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {

        @Test
        @DisplayName("성공")
        void loginSuccess() {
            when(userRepository.findByEmailAndLoginType(email, LoginType.BASIC))
                    .thenReturn(Optional.of(mockUser));

            when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
            when(jwtProvider.generateToken(userId, "ROLE_USER")).thenReturn(accessToken);
            when(jwtProvider.generateRefreshToken(userId)).thenReturn(refreshToken);
            when(jwtProvider.getTokenExpiry(refreshToken)).thenReturn(futureTimestamp);

            TokenResponseDto result = authService.login(email, password, LoginType.BASIC);

            assertThat(result.getAccessToken()).isEqualTo(accessToken);
            assertThat(result.getRefreshToken()).isEqualTo(refreshToken);
            assertThat(result.getUser().getEmail()).isEqualTo(email);

            verify(redisService).save(eq(userId), eq(refreshToken), anyLong());
        }

        @Test
        @DisplayName("실패 - 유저 없음")
        void loginUserNotFound() {
            when(userRepository.findByEmailAndLoginType(email, LoginType.BASIC))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> authService.login(email, password, LoginType.BASIC))
                    .isInstanceOf(ApplicationException.class)
                    .hasMessageContaining(UserErrorCase.USER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("실패 - 비밀번호 불일치")
        void loginInvalidPassword() {
            when(userRepository.findByEmailAndLoginType(email, LoginType.BASIC))
                    .thenReturn(Optional.of(mockUser));
            when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

            assertThatThrownBy(() -> authService.login(email, password, LoginType.BASIC))
                    .isInstanceOf(ApplicationException.class)
                    .hasMessageContaining(AuthErrorCase.INVALID_PASSWORD.getMessage());
        }
    }

    @Nested
    @DisplayName("재발급 테스트")
    class ReissueTest {

        @Test
        @DisplayName("성공")
        void reissueSuccess() {
            when(jwtProvider.validateToken(refreshToken)).thenReturn(true);
            when(jwtProvider.getUserId(refreshToken)).thenReturn(userId);
            when(redisService.isSame(userId, refreshToken)).thenReturn(true);

            when(jwtProvider.generateToken(userId, "ROLE_USER")).thenReturn(accessToken);
            when(jwtProvider.generateRefreshToken(userId)).thenReturn(refreshToken);
            when(jwtProvider.getTokenExpiry(refreshToken)).thenReturn(futureTimestamp);

            TokenResponseDto result = authService.reissue(refreshToken);

            assertThat(result.getAccessToken()).isEqualTo(accessToken);
            assertThat(result.getRefreshToken()).isEqualTo(refreshToken);

            verify(redisService).save(eq(userId), eq(refreshToken), anyLong());
        }

        @Test
        @DisplayName("실패 - 유효하지 않은 토큰")
        void reissueInvalidToken() {
            when(jwtProvider.validateToken(refreshToken)).thenReturn(false);

            assertThatThrownBy(() -> authService.reissue(refreshToken))
                    .isInstanceOf(ApplicationException.class)
                    .hasMessageContaining(AuthErrorCase.INVALID_REFRESH_TOKEN.getMessage());
        }

        @Test
        @DisplayName("실패 - Redis와 일치하지 않음")
        void reissueNotRedis() {
            when(jwtProvider.validateToken(refreshToken)).thenReturn(true);
            when(jwtProvider.getUserId(refreshToken)).thenReturn(userId);
            when(redisService.isSame(userId, refreshToken)).thenReturn(false);

            assertThatThrownBy(() -> authService.reissue(refreshToken))
                    .isInstanceOf(ApplicationException.class)
                    .hasMessageContaining(AuthErrorCase.INVALID_REFRESH_TOKEN.getMessage());
        }
    }

    @Test
    @DisplayName("로그아웃 - 성공")
    void logoutSuccess() {
        authService.logout(userId);
        verify(redisService).delete(userId);
    }
}
