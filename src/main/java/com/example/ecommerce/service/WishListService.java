package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.model.WishList;

public interface WishListService
{
        WishList createWishList(User user);

        WishList getWishListbyUserId(User user);

        WishList addProductToWishList(User user , Product product);
}
