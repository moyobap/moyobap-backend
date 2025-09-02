package com.moyobab.server.menucategory.repository;

import com.moyobab.server.menucategory.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
}
