package com.moyobab.server.place.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceResponseDto {
    private String placeName;
    private String addressName;
    private String roadAddressName;
    private String phone;
    private double latitude;
    private double longitude;
}