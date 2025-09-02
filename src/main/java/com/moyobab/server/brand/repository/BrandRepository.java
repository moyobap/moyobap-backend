package com.moyobab.server.brand.repository;

import com.moyobab.server.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    List<Brand> findByCategory(String category);
}
