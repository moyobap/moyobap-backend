package com.moyobab.server.place.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class KakaoPlaceDto {
    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("road_address_name")
    private String roadAddressName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("x")
    private String longitude;

    @JsonProperty("y")
    private String latitude;
}
