package com.example.ecommerce.service;

import com.example.ecommerce.model.HomeCategory;

import java.util.List;

public interface HomeCategoryService {

    HomeCategory createHomeCategory(HomeCategory homeCategory);
    List<HomeCategory> createCategories(List<HomeCategory> homeCategories);
    HomeCategory updateHomeCategory(HomeCategory homeCategory,Integer id) throws Exception;
    List<HomeCategory> getAllHomeCategories();
}
