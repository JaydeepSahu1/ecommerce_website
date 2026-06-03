package com.example.ecommerce.service;

import com.example.ecommerce.model.Home;
import com.example.ecommerce.model.HomeCategory;

import java.util.List;

public interface HomeService
{
    public Home createHomepageData(List<HomeCategory> allCategories);

}
