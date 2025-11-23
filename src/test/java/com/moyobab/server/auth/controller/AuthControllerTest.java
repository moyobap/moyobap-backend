package com.moyobab.server.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyobab.server.user.dto.UserSignUpRequestDto;
import com.moyobab.server.user.entity.LoginType;
import com.moyobab.server.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserService userService;

    @BeforeEach
    void setUp() {
        UserSignUpRequestDto dto = UserSignUpRequestDto.builder()
                .email("test5@naver.com")
                .password("test!1234")
                .username("testuser5")
                .nickname("테스트유저5")
                .phoneNumber("010-1111-2222")
                .birthDate(LocalDate.of(2001, 1, 1))
                .loginType(LoginType.BASIC)
                .build();

        userService.signup(dto);
    }

    @Test
    @DisplayName("로그인 성공 - accessToken, refreshToken, user 정보 반환")
    void loginSuccess() throws Exception {
        String json = """
            {
                "email": "test5@naver.com",
                "password": "test!1234",
                "loginType": "BASIC"
            }
        """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andExpect(jsonPath("$.data.user.userId").exists())
                .andExpect(jsonPath("$.data.user.email").value("test5@naver.com"))
                .andExpect(jsonPath("$.data.user.nickname").value("테스트유저5"));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 오류")
    void loginFailWrongPassword() throws Exception {
        String json = """
            {
                "email": "test5@naver.com",
                "password": "test!2345",
                "loginType": "BASIC"
            }
        """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 없음")
    void loginFailInvalidEmail() throws Exception {
        String json = """
            {
                "email": "test9999@naver.com",
                "password": "test!1234",
                "loginType": "BASIC"
            }
        """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }
}

