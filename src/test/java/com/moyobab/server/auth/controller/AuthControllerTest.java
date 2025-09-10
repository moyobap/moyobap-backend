package com.moyobab.server.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyobab.server.user.dto.UserLoginRequestDto;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserService userService;

    @BeforeEach
    void setUp() {
        UserSignUpRequestDto dto = new UserSignUpRequestDto();
        dto.setEmail("test5@naver.com");
        dto.setPassword("test!1234");
        dto.setUsername("testuser5");
        dto.setNickname("테스트유저5");
        dto.setPhoneNumber("010-1111-2222");
        dto.setBirthDate(LocalDate.of(2001, 1, 1));
        dto.setLoginType(LoginType.BASIC);

        userService.signup(dto);
    }

    @Test
    @DisplayName("로그인 성공 - accessToken, refreshToken 발급")
    void loginSuccess() throws Exception {
        UserLoginRequestDto loginRequest = UserLoginRequestDto.builder()
                .email("test5@naver.com")
                .password("test!1234")
                .loginType(LoginType.BASIC)
                .build();

        String json = objectMapper.writeValueAsString(loginRequest);

        ResultActions result = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 틀림")
    void loginFailWrongPassword() throws Exception {
        UserLoginRequestDto loginRequest = UserLoginRequestDto.builder()
                .email("test5@naver.com")
                .password("test!2345")
                .loginType(LoginType.BASIC)
                .build();

        String json = objectMapper.writeValueAsString(loginRequest);

        ResultActions result = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void loginFailInvalidEmail() throws Exception {
        UserLoginRequestDto loginRequest = UserLoginRequestDto.builder()
                .email("test9999@naver.com")
                .password("test!1234")
                .loginType(LoginType.BASIC)
                .build();

        String json = objectMapper.writeValueAsString(loginRequest);

        ResultActions result = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpect(status().isNotFound());
    }
}