package com.moyobab.server.user.service;

import com.moyobab.server.global.exception.ApplicationException;
import com.moyobab.server.user.dto.UserSignUpRequestDto;
import com.moyobab.server.user.entity.LoginType;
import com.moyobab.server.user.entity.User;
import com.moyobab.server.user.exception.UserErrorCase;
import com.moyobab.server.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserSignUpRequestDto validRequest;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        validRequest = UserSignUpRequestDto.builder()
                .email("test7@naver.com")
                .password("test!1234")
                .username("testuser7")
                .nickname("테스트유저7")
                .phoneNumber("010-1234-5678")
                .birthDate(LocalDate.of(2002, 2, 2))
                .loginType(LoginType.BASIC)
                .build();
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class SignupTest {

        @Test
        @DisplayName("성공")
        void signupSuccess() {
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByNickname(anyString())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");

            userService.signup(validRequest);

            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("실패 - 이메일 중복")
        void emailDuplicate() {
            when(userRepository.existsByEmail(anyString())).thenReturn(true);

            assertThatThrownBy(() -> userService.signup(validRequest))
                    .isInstanceOf(ApplicationException.class)
                    .hasMessageContaining(UserErrorCase.EMAIL_DUPLICATED.getMessage());
        }

        @Test
        @DisplayName("실패 - 닉네임 중복")
        void nicknameDuplicate() {
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByNickname(anyString())).thenReturn(true);

            assertThatThrownBy(() -> userService.signup(validRequest))
                    .isInstanceOf(ApplicationException.class)
                    .hasMessageContaining(UserErrorCase.NICKNAME_DUPLICATED.getMessage());
        }

        @Test
        @DisplayName("실패 - DB 제약 (이메일)")
        void duplicateKey_email() {
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByNickname(anyString())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");

            DataIntegrityViolationException exception = new DataIntegrityViolationException("duplicate key: email");
            when(userRepository.save(any(User.class))).thenThrow(exception);

            assertThatThrownBy(() -> userService.signup(validRequest))
                    .isInstanceOf(ApplicationException.class)
                    .hasMessageContaining(UserErrorCase.EMAIL_DUPLICATED.getMessage());
        }

        @Test
        @DisplayName("실패 - DB 제약 (닉네임)")
        void duplicateKey_nickname() {
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByNickname(anyString())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");

            DataIntegrityViolationException exception = new DataIntegrityViolationException("duplicate key: nickname");
            when(userRepository.save(any(User.class))).thenThrow(exception);

            assertThatThrownBy(() -> userService.signup(validRequest))
                    .isInstanceOf(ApplicationException.class)
                    .hasMessageContaining(UserErrorCase.NICKNAME_DUPLICATED.getMessage());
        }
    }

    @Nested
    @DisplayName("중복 체크 테스트")
    class DuplicateCheck {

        @Test
        @DisplayName("이메일 사용 가능")
        void emailAvailable() {
            when(userRepository.existsByEmail("test99@naver.com")).thenReturn(false);
            assertThat(userService.isEmailAvailable("test99@naver.com")).isTrue();
        }

        @Test
        @DisplayName("이메일 중복")
        void emailDuplicated() {
            when(userRepository.existsByEmail("test99@naver.com")).thenReturn(true);
            assertThat(userService.isEmailAvailable("test99@naver.com")).isFalse();
        }

        @Test
        @DisplayName("닉네임 사용 가능")
        void nicknameAvailable() {
            when(userRepository.existsByNickname("닉네임")).thenReturn(false);
            assertThat(userService.isNicknameAvailable("닉네임")).isTrue();
        }

        @Test
        @DisplayName("닉네임 중복")
        void nicknameDuplicated() {
            when(userRepository.existsByNickname("닉네임")).thenReturn(true);
            assertThat(userService.isNicknameAvailable("닉네임")).isFalse();
        }
    }
}
