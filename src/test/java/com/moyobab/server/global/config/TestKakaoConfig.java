package com.moyobab.server.global.config;

import com.moyobab.server.global.config.kakao.KakaoPlaceClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestKakaoConfig {

    @Bean
    public KakaoPlaceClient kakaoPlaceClient() {
        KakaoPlaceClient mock = Mockito.mock(KakaoPlaceClient.class);

        Mockito.when(mock.searchKeyword(Mockito.any(), Mockito.any(), Mockito.any(),
                        Mockito.any(), Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn("{\"documents\": []}");

        return mock;
    }
}
