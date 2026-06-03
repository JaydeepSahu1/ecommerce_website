package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Coupon;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.CouponService;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon")
public class AdminCouponController {

    private final CouponService couponService;
    private final UserService userService;
    private final CartService cartService;

    @PostMapping("/apply")
    public ResponseEntity<Cart> applyCoupon(
            @RequestParam String apply,
            @RequestParam String code,
            @RequestParam double ordervalue,
            @RequestHeader ("Authorization") String jwt) throws Exception {
        User user = userService.findUserbyJwtToken(jwt);
        Cart cart;

        if(apply.equals("true"))
        {
            cart=couponService.applyCoupon(code,ordervalue,user);
        }
        else
        {
            cart = couponService.removeCoupon(code,user);
        }
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Coupon> createCoupon(
            @RequestBody Coupon coupon)
    {
        Coupon createdcoupon =couponService.createCoupon(coupon);

        return ResponseEntity.ok(createdcoupon);
    }

    @DeleteMapping
}
