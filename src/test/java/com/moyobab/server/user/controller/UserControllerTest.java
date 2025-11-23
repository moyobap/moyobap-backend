/*
package com.moyobab.server.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyobab.server.global.config.TestKakaoConfig;
import com.moyobab.server.user.dto.UserSignUpRequestDto;
import com.moyobab.server.user.entity.LoginType;
import com.moyobab.server.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@Import(TestKakaoConfig.class)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserService userService;

    @BeforeEach
    void setup() {
        UserSignUpRequestDto dto = UserSignUpRequestDto.builder()
                .email("test1@naver.com")
                .password("test1!1234")
                .username("testuser1")
                .nickname("테스트유저1")
                .phoneNumber("010-1234-5678")
                .birthDate(LocalDate.of(1999, 1, 1))
                .loginType(LoginType.BASIC)
                .build();

        userService.signup(dto);
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccess() throws Exception {
        UserSignUpRequestDto request = UserSignUpRequestDto.builder()
                .email("test2@naver.com")
                .password("test!2345")
                .username("testuser2")
                .nickname("테스트유저2")
                .phoneNumber("010-2345-7890")
                .birthDate(LocalDate.of(2000, 1, 1))
                .loginType(LoginType.BASIC)
                .build();

        String json = """
        {
            "email": "test2@naver.com",
            "password": "test!2345",
            "username": "testuser2",
            "nickname": "테스트유저2",
            "phoneNumber": "010-2345-7890",
            "birthDate": "2000-01-01",
            "loginType": "BASIC"
        }
        """;

        mockMvc.perform(post("/api/v1/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("회원가입 성공"));
    }

    @Test
    @DisplayName("이메일 중복 확인 - 사용 가능")
    void emailCheckAvailable() throws Exception {
        mockMvc.perform(get("/api/v1/users/check-email")
                        .param("email", "test3@naver.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.available").value(true));
    }

    @Test
    @DisplayName("이메일 중복 확인 - 중복")
    void emailCheckDuplicated() throws Exception {
        mockMvc.perform(get("/api/v1/users/check-email")
                        .param("email", "test1@naver.com"))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 사용 가능")
    void nicknameCheckAvailable() throws Exception {
        mockMvc.perform(get("/api/v1/users/check-nickname")
                        .param("nickname", "새로운닉네임"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.available").value(true));
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 중복")
    void nicknameCheckDuplicated() throws Exception {
        mockMvc.perform(get("/api/v1/users/check-nickname")
                        .param("nickname", "테스트유저1"))
                .andExpect(status().isConflict());
    }
}


 */
