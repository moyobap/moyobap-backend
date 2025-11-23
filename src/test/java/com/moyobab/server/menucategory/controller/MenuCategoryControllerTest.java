package com.moyobab.server.menucategory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyobab.server.menucategory.entity.MenuCategoryType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class MenuCategoryControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("메뉴 카테고리 전체 조회 - 성공")
    void getAllMenuCategories() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/menu-categories")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(MenuCategoryType.values().length))
                .andExpect(jsonPath("$.data[0].name").value(MenuCategoryType.values()[0].name()))
                .andExpect(jsonPath("$.data[0].displayName").value(MenuCategoryType.values()[0].getDisplayName()))
                .andExpect(jsonPath("$.data[0].kakaoCategoryKeyword").value(MenuCategoryType.values()[0].getKakaoCategoryKeyword()));
    }
}
