package com.moyobab.server.place.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyobab.server.place.dto.KakaoPlaceDto;
import com.moyobab.server.place.dto.PlaceResponseDto;
import com.moyobab.server.global.config.kakao.KakaoPlaceClient;
import com.moyobab.server.menucategory.entity.MenuCategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceSearchService {

    private final KakaoPlaceClient kakaoClient;
    private final ObjectMapper objectMapper;

    public List<PlaceResponseDto> searchByCategory(MenuCategoryType category, Double x, Double y, Integer radius) {
        Set<String> seen = new HashSet<>();

        // 1차적으로 category + keyword 기반 검색
        List<PlaceResponseDto> primaryResults = fetchAndParse(
                category.getKakaoCategoryKeyword(),
                category.getKakaoCategoryGroupCode(),
                x, y, radius
        );

        primaryResults.forEach(p -> seen.add(uniqueKey(p)));

        // 2차적으로 fallback -> keyword only 검색 (category_group_code 없이)
        List<PlaceResponseDto> fallbackResults = fetchAndParse(
                category.getKakaoCategoryKeyword(),
                null,
                x, y, radius
        );

        // 3차적으로 중복 제거 및 결합
        List<PlaceResponseDto> finalResults = new ArrayList<>(primaryResults);

        for (PlaceResponseDto dto : fallbackResults) {
            if (!seen.contains(uniqueKey(dto))) {
                finalResults.add(dto);
                seen.add(uniqueKey(dto));
            }
        }

        return finalResults;
    }

    private List<PlaceResponseDto> fetchAndParse(String keyword, String categoryCode, Double x, Double y, Integer radius) {
        try {
            String response = kakaoClient.searchKeyword(keyword, x, y, radius, categoryCode, 15, 1);

            JsonNode root = objectMapper.readTree(response);
            JsonNode documents = root.path("documents");

            List<KakaoPlaceDto> kakaoPlaces = objectMapper
                    .readerForListOf(KakaoPlaceDto.class)
                    .readValue(documents.toString());

            return kakaoPlaces.stream()
                    .map(dto -> {
                        double lat = 0.0;
                        double lon = 0.0;
                        try {
                            lat = Double.parseDouble(dto.getLatitude());
                            lon = Double.parseDouble(dto.getLongitude());
                        } catch (NumberFormatException ignored) {}

                        return PlaceResponseDto.builder()
                                .placeName(dto.getPlaceName())
                                .addressName(dto.getAddressName())
                                .roadAddressName(dto.getRoadAddressName())
                                .phone(dto.getPhone())
                                .latitude(lat)
                                .longitude(lon)
                                .build();
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("카카오 API 파싱 실패", e);
        }
    }

    // 중복 제거 기준 정의 추가
    private String uniqueKey(PlaceResponseDto dto) {
        if (dto.getPhone() != null && !dto.getPhone().isBlank()) {
            return dto.getPhone().trim();
        }
        return dto.getLatitude() + "," + dto.getLongitude();
    }
}
