package com.example.ecommerce.repository;

import com.example.ecommerce.model.HomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Integer> {

    HomeCategory findById(Long id);

}
