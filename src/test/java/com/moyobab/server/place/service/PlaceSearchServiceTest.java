package com.moyobab.server.place.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyobab.server.global.config.kakao.KakaoPlaceClient;
import com.moyobab.server.menucategory.entity.MenuCategoryType;
import com.moyobab.server.place.dto.PlaceResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlaceSearchServiceTest {

    @Test
    @DisplayName("카테고리로 검색 결과 반환 성공")
    void searchByCategorySuccess() throws Exception {
        // given
        KakaoPlaceClient mockClient = Mockito.mock(KakaoPlaceClient.class);
        ObjectMapper objectMapper = new ObjectMapper();
        PlaceSearchService service = new PlaceSearchService(mockClient, objectMapper);

        String mockJson = """
            {
              "documents": [
                {
                  "place_name": "BBQ치킨 홍대점",
                  "address_name": "서울 마포구 양화로",
                  "road_address_name": "서울 마포구 양화로 123",
                  "phone": "02-123-4567",
                  "x": "126.920678",
                  "y": "37.556153"
                },
                {
                  "place_name": "BHC 치킨 신촌점",
                  "address_name": "서울 서대문구 신촌로",
                  "road_address_name": "서울 서대문구 신촌로 45",
                  "phone": "02-987-6543",
                  "x": "126.938500",
                  "y": "37.559000"
                }
              ]
            }
        """;

        Mockito.when(mockClient.searchKeyword(
                Mockito.anyString(),
                Mockito.anyDouble(),
                Mockito.anyDouble(),
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyInt(),
                Mockito.anyInt()
        )).thenReturn(mockJson);

        // when
        List<PlaceResponseDto> results = service.searchByCategory(
                MenuCategoryType.CHICKEN,
                126.9300,
                37.5500,
                2000
        );

        // then
        assertEquals(2, results.size());

        PlaceResponseDto place1 = results.get(0);
        assertEquals("BBQ치킨 홍대점", place1.getPlaceName());
        assertEquals("서울 마포구 양화로", place1.getAddressName());

        PlaceResponseDto place2 = results.get(1);
        assertEquals("BHC 치킨 신촌점", place2.getPlaceName());
        assertEquals("서울 서대문구 신촌로", place2.getAddressName());
    }
}