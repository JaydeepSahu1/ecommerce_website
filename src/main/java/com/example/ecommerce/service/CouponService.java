package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Coupon;
import com.example.ecommerce.model.User;

import java.util.List;

public interface CouponService {

    Cart applyCoupon(String code, double ordervalue, User user) throws Exception;

    Cart removeCoupon(String code,User user) throws Exception;

    Coupon findCouponbyId(Long id) throws Exception;

    Coupon createCoupon(Coupon coupon);

    List<Coupon> findallCoupons();

    void deleteCouponbyId(Long id) throws Exception;
}
