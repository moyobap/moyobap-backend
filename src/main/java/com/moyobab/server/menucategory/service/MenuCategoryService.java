package com.moyobab.server.menucategory.service;

import com.moyobab.server.menucategory.dto.MenuCategoryResponseDto;
import com.moyobab.server.menucategory.entity.MenuCategoryType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuCategoryService {

    public List<MenuCategoryResponseDto> getAllCategories() {
        return List.of(MenuCategoryType.values()).stream()
                .map(MenuCategoryResponseDto::from)
                .collect(Collectors.toList());
    }
}
