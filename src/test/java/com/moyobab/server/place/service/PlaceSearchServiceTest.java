package com.moyobab.server.place.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyobab.server.menucategory.entity.MenuCategoryType;
import com.moyobab.server.place.dto.PlaceResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PlaceSearchServiceTest {

    private KakaoPlaceClient kakaoPlaceClient;
    private ObjectMapper objectMapper;
    private PlaceSearchService placeSearchService;

    @BeforeEach
    void setUp() {
        kakaoPlaceClient = mock(KakaoPlaceClient.class);
        objectMapper = new ObjectMapper();
        placeSearchService = new PlaceSearchService(kakaoPlaceClient, objectMapper);
    }

    @Test
    @DisplayName("카테고리 기반 음식점 검색 성공")
    void searchByCategorySuccess() {
        String mockResponse = """
        {
          "documents": [
            {
              "place_name": "BHC 치킨 강남점",
              "address_name": "서울 강남구 테헤란로",
              "road_address_name": "서울 강남구 테헤란로 123",
              "phone": "02-123-4567",
              "x": "127.0276",
              "y": "37.4979"
            }
          ]
        }
        """;

        when(kakaoPlaceClient.searchKeyword(
                eq("치킨"), any(), any(), any(), any(), anyInt(), anyInt()
        )).thenReturn(mockResponse);

        List<PlaceResponseDto> result = placeSearchService.searchByCategory(
                MenuCategoryType.CHICKEN, 127.0, 37.0, 1000);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getPlaceName()).isEqualTo("BHC 치킨 강남점");
    }
}
