package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.Deal;
import com.example.ecommerce.model.HomeCategory;
import com.example.ecommerce.repository.DealRepository;
import com.example.ecommerce.repository.HomeCategoryRepository;
import com.example.ecommerce.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService
{
    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public List<Deal> getAllDeals()
    {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal)
    {
        HomeCategory category=homeCategoryRepository.findById(deal.getCategory().getId());
        Deal newDeal = dealRepository.save(deal);
        newDeal.setCategory(category);
        newDeal.setDiscount(deal.getDiscount());

        return dealRepository.save(newDeal);
    }

    @Override
    public Deal updateDeal(Deal deal,Long id) throws Exception {
        Deal existingDeal =dealRepository.findById(deal.getId()).get();
        HomeCategory category=homeCategoryRepository.findById(deal.getCategory().getId());

        if(existingDeal!=null){
            if(deal.getDiscount()!=null){
                existingDeal.setDiscount(deal.getDiscount());
            }
            if(category!=null){
                existingDeal.setCategory(category);
            }

            return dealRepository.save(existingDeal);
        }
        throw new Exception("Deal not found");
    }

    @Override
    public void deleteDeal(Long id)
    {
        Deal deal=dealRepository.findById(id).get();
        dealRepository.delete(deal);
    }
}
