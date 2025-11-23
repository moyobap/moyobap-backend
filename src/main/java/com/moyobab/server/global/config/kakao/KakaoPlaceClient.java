package com.moyobab.server.global.config.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@ConditionalOnProperty(name = "kakao.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class KakaoPlaceClient {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private final WebClient webClient;

    public String searchKeyword(String query, Double x, Double y, Integer radius,
                                String categoryGroupCode, int size, int page) {

        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder
                            .scheme("https")
                            .host("dapi.kakao.com")
                            .path("/v2/local/search/keyword.json")
                            .queryParam("query", query)
                            .queryParam("size", size)
                            .queryParam("page", page);

                    if (x != null && y != null) {
                        uriBuilder.queryParam("x", x).queryParam("y", y);
                    }
                    if (radius != null) {
                        uriBuilder.queryParam("radius", radius);
                    }
                    if (categoryGroupCode != null) {
                        uriBuilder.queryParam("category_group_code", categoryGroupCode);
                    }

                    return uriBuilder.build();
                })
                .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoApiKey)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        clientResponse -> clientResponse.createException().flatMap(Mono::error))
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .block();
    }
}