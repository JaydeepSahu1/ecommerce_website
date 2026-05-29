package com.example.ecommerce.service;

import com.example.ecommerce.model.CartItem;

public interface CartItemService {

    CartItem updateCartItem(Long userId, Long cartid,CartItem cartItem) throws Exception;

    void removeCartItem(Long userId, Long cartItemId) throws Exception;

    CartItem findCartItemById(Long Id) throws Exception;
}
