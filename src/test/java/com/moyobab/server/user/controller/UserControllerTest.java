package com.moyobab.server.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyobab.server.user.dto.UserSignUpRequestDto;
import com.moyobab.server.user.entity.LoginType;
import com.moyobab.server.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserService userService;

    @BeforeEach
    void setup() {
        // 테스트용 유저 등록
        UserSignUpRequestDto dto = new UserSignUpRequestDto();
        dto.setEmail("test@naver.com");
        dto.setPassword("test!1234");
        dto.setUsername("testuser1");
        dto.setNickname("테스트유저1");
        dto.setPhoneNumber("010-1234-5678");
        dto.setBirthDate(LocalDate.of(1999, 1, 1));
        dto.setLoginType(LoginType.BASIC);

        userService.signup(dto);
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccess() throws Exception {
        UserSignUpRequestDto request = new UserSignUpRequestDto();
        request.setEmail("test@naver.com");
        request.setPassword("test!1234");
        request.setUsername("testuser1");
        request.setNickname("테스트유저1");
        request.setPhoneNumber("010-1234-5678");
        request.setBirthDate(LocalDate.of(1999, 1, 1));

        String json = objectMapper.writeValueAsString(request);

        ResultActions result = mockMvc.perform(post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("회원가입 성공"));
    }

    @Test
    @DisplayName("이메일 중복 확인 - 사용 가능")
    void emailCheckAvailable() throws Exception {
        String email = "test2@naver.com";

        ResultActions result = mockMvc.perform(get("/api/v1/users/check-email")
                .param("email", email));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.available").value(true));
    }

    @Test
    @DisplayName("이메일 중복 확인 - 중복")
    void emailCheckDuplicated() throws Exception {
        String email = "test@naver.com"; // 이미 가입된 이메일로 테스트

        ResultActions result = mockMvc.perform(get("/api/v1/users/check-email")
                .param("email", email));

        result.andExpect(status().isConflict());
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 사용 가능")
    void nicknameCheckAvailable() throws Exception {
        String nickname = "테스트유저2";

        ResultActions result = mockMvc.perform(get("/api/v1/users/check-nickname")
                .param("nickname", nickname));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.available").value(true));
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 중복")
    void nicknameCheckDuplicated() throws Exception {
        String nickname = "테스트유저1"; // 이미 사용 중인 닉네임

        ResultActions result = mockMvc.perform(get("/api/v1/users/check-nickname")
                .param("nickname", nickname));

        result.andExpect(status().isConflict());
    }
}