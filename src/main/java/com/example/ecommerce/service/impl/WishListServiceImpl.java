package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.model.WishList;
import com.example.ecommerce.repository.WishListRepository;
import com.example.ecommerce.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;

    @Override
    public WishList createWishList(User user) {
        WishList wishList = new WishList();
        wishList.setUser(user);
        return wishListRepository.save(wishList);
    }

    @Override
    public WishList getWishListbyUserId(User user)
    {
        WishList wishlist= wishListRepository.findByUserId(user.getId());
        if(wishlist==null)
        {
            wishlist=createWishList(user);
        }
        return wishlist;
    }

    @Override
    public WishList addProductToWishList(User user, Product product) {
        WishList wishList = wishListRepository.findByUserId(user.getId());

        if(wishList.getProducts().contains(product))
        {
            wishList.getProducts().remove(product);
        }
        else wishList.getProducts().add(product);

        return wishListRepository.save(wishList);
    }
}
