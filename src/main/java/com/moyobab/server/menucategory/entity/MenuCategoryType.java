package com.moyobab.server.menucategory.entity;

import lombok.Getter;

@Getter
public enum MenuCategoryType {
    CHICKEN("치킨", "치킨"),
    PIZZA("피자", "피자"),
    KOREAN("한식", "한식"),
    JAPANESE("일식", "돈까스"),
    CHINESE("중식", "중국집"),
    SNACK("분식", "떡볶이"),
    CAFE("카페/디저트", "카페"),
    FASTFOOD("패스트푸드", "햄버거"),
    MEAT("고기", "고깃집"),
    STEW("찌개/탕", "찌개"),
    LUNCHBOX("도시락", "도시락"),
    NIGHT("야식", "야식"),
    ASIAN("아시안", "베트남음식"),
    PUB("술집", "호프");

    private final String displayName;
    private final String kakaoCategoryKeyword; // 카카오 카테고리 검색 외부 api 도입 예정

    MenuCategoryType(String displayName, String kakaoCategoryKeyword) {
        this.displayName = displayName;
        this.kakaoCategoryKeyword = kakaoCategoryKeyword;
    }
}

