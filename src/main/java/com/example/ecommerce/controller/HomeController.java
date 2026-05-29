package com.example.ecommerce.controller;

import com.example.ecommerce.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ApiResponse HomeControllerHandler(){
        ApiResponse apiresponse = new ApiResponse();
        apiresponse.setMessage("Welcome");
        return apiresponse;
    }
}
