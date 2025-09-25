package com.moyobab.server.menucategory.entity;

import lombok.Getter;

@Getter
public enum MenuCategoryType {
    CHICKEN("치킨", "치킨", "FD6"),
    PIZZA("피자", "피자", "FD6"),
    KOREAN("한식", "한식", "FD6"),
    CHINESE("중식", "중식", "FD6"),
    CAFE("카페/디저트", "카페", "CE7"),
    FASTFOOD("패스트푸드", "햄버거", "FD6"),
    SNACK("분식", "떡볶이", "FD6"),
    SUSHI("돈까스/회", "회", "FD6"),
    STEW("찜/탕", "탕", "FD6"),
    NIGHT("야식", "야식", "FD6"),
    MEAT("고기", "고기구이", "FD6"),
    LUNCHBOX("도시락", "도시락", "FD6"),
    WESTERN("양식", "양식", "FD6"),
    ASIAN("아시안", "아시안", "FD6");

    private final String displayName;
    private final String kakaoCategoryKeyword;
    private final String kakaoCategoryGroupCode;

    MenuCategoryType(String displayName, String kakaoCategoryKeyword, String kakaoCategoryGroupCode) {
        this.displayName = displayName;
        this.kakaoCategoryKeyword = kakaoCategoryKeyword;
        this.kakaoCategoryGroupCode = kakaoCategoryGroupCode;
    }
}