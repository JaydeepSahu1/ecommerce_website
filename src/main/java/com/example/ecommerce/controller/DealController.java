package com.example.ecommerce.controller;

import com.example.ecommerce.model.Deal;
import com.example.ecommerce.repository.DealRepository;
import com.example.ecommerce.response.ApiResponse;
import com.example.ecommerce.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController
{
    private final DealService dealService;

    @PostMapping("/{id}")
    public ResponseEntity<Deal> createDeal(
            @RequestBody Deal deal
    )
    {
        Deal createDeal=dealService.createDeal(deal);
        return new ResponseEntity<>(createDeal, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(
            @PathVariable Long id,
            @RequestBody Deal deal
    ) throws Exception {
        Deal updatedeal=dealService.updateDeal(deal,id);
        return ResponseEntity.ok(updatedeal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDeal(
            @PathVariable Long id
    ) throws Exception
    {
        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setMessage("Delete Deal Successfully");


        return new ResponseEntity<>(apiResponse,HttpStatus.ACCEPTED);
    }
}
