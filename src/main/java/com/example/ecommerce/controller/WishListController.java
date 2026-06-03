package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.model.WishList;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishListController
{
    private final WishListService wishListService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<WishList> getWishListbyUserId(
            @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user =userService.findUserbyJwtToken(jwt);
        WishList wishlist=wishListService.getWishListbyUserId(user);

        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<WishList> addProductToWishList(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception
    {
        Product product =  productService.findProductbyId(productId);
        User user = userService.findUserbyJwtToken(jwt);

        WishList updateWishList = wishListService.addProductToWishList(user, product);

        return ResponseEntity.ok(updateWishList);
    }
}
