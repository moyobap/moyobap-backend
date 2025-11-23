package com.moyobab.server.place.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyobab.server.menucategory.entity.MenuCategoryType;
import com.moyobab.server.place.dto.PlaceResponseDto;
import com.moyobab.server.place.service.PlaceSearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PlaceSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaceSearchService placeSearchService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("음식점 검색 API - 성공")
    void searchPlacesSuccess() throws Exception {
        List<PlaceResponseDto> mockResults = List.of(
                PlaceResponseDto.builder()
                        .placeName("BBQ치킨 홍대점")
                        .addressName("서울 마포구 양화로")
                        .roadAddressName("서울 마포구 양화로 123")
                        .phone("02-123-4567")
                        .longitude(126.920678)
                        .latitude(37.556153)
                        .build(),

                PlaceResponseDto.builder()
                        .placeName("BHC 치킨 신촌점")
                        .addressName("서울 서대문구 신촌로")
                        .roadAddressName("서울 서대문구 신촌로 45")
                        .phone("02-987-6543")
                        .longitude(126.9385)
                        .latitude(37.559)
                        .build()
        );

        Mockito.when(placeSearchService.searchByCategory(
                Mockito.eq(MenuCategoryType.CHICKEN),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyInt()
        )).thenReturn(mockResults);

        mockMvc.perform(get("/api/v1/places/search")
                        .param("category", "CHICKEN")
                        .param("x", "127.02758")
                        .param("y", "37.49794")
                        .param("radius", "1500")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].placeName").value("BBQ치킨 홍대점"))
                .andExpect(jsonPath("$.data[1].placeName").value("BHC 치킨 신촌점"));
    }

    @Test
    @DisplayName("음식점 검색 API - 실패 (카테고리 누락)")
    void searchPlacesMissingCategory() throws Exception {
        mockMvc.perform(get("/api/v1/places/search")
                        .param("x", "127.02758")
                        .param("y", "37.49794")
                        .param("radius", "1500")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(9001));
    }

    @Test
    @DisplayName("음식점 검색 API - 실패 (좌표 누락)")
    void searchPlacesMissingCoordinates() throws Exception {
        mockMvc.perform(get("/api/v1/places/search")
                        .param("category", "CHICKEN")
                        .param("radius", "1500")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(9002));
    }

    @Test
    @DisplayName("음식점 검색 API - 실패 (검색 결과 없음)")
    void searchPlacesNotFound() throws Exception {
        Mockito.when(placeSearchService.searchByCategory(
                Mockito.eq(MenuCategoryType.CHICKEN),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.anyInt()
        )).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/places/search")
                        .param("category", "CHICKEN")
                        .param("x", "127.02758")
                        .param("y", "37.49794")
                        .param("radius", "1500")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(9004));
    }
}

