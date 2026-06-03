package com.example.ecommerce.service;

import com.example.ecommerce.model.Deal;

import java.util.List;

public interface DealService
{
    public List<Deal> getAllDeals();
    Deal createDeal(Deal deal);
    Deal updateDeal(Deal deal,Long id) throws Exception;
    void deleteDeal(Long id);
}
