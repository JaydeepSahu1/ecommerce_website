package com.example.ecommerce.controller;

import com.example.ecommerce.model.Home;
import com.example.ecommerce.model.HomeCategory;
import com.example.ecommerce.service.HomeCategoryService;
import com.example.ecommerce.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class HomeCategoryController {

    private final HomeCategoryService homeCategoryService;
    private HomeService homeService;

    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomeCategories
            (@RequestBody List<HomeCategory> homeCategories) throws Exception
    {
        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
        Home home = homeService.createHomepageData(categories);
        return new ResponseEntity<>(home, HttpStatus.ACCEPTED);
    }

    @GetMapping("/admin/home-category")
    public ResponseEntity<List<HomeCategory>> getHomeCategories() throws Exception
    {
        List<HomeCategory> categories = homeCategoryService.getAllHomeCategories();
        return ResponseEntity.ok(categories);
    }

    @PatchMapping("/admin/home-category/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(
            @PathVariable Integer id, @RequestBody HomeCategory homeCategory
    ) throws Exception
    {
        HomeCategory updatecategory=homeCategoryService.updateHomeCategory(homeCategory,id);
        return ResponseEntity.ok(updatecategory);
    }
}
