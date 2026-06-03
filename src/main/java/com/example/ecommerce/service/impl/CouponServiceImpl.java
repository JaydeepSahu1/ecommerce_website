package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Coupon;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.CouponRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Override
    public Cart applyCoupon(String code, double ordervalue, User user) throws Exception {
        Coupon coupon=couponRepository.findByCode(code);

        Cart cart=cartRepository.findByUserId(user.getId());
        if(coupon==null)
        {
            throw new Exception("Coupon not valid");
        }
        if(user.getUsedCoupons().contains(coupon))
        {
            throw new Exception("Coupon already used");
        }
        if(ordervalue<coupon.getMinimumOrderValue())
        {
            throw new Exception("Order value is too low"+coupon.getMinimumOrderValue());
        }
        if(coupon.isActive()
            && LocalDate.now().isAfter(coupon.getValidityStartDate())
                && LocalDate.now().isBefore(coupon.getValidityEndDate()))
        {
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);

            int discountedPrice = (int) ((cart.getTotalSellingPrice()*coupon.getDiscountPercentage())/100);
            cart.setTotalSellingPrice(cart.getTotalSellingPrice()-discountedPrice);
            cartRepository.save(cart);
            return cart;
        }
        throw new Exception("Coupon not valid");
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception
    {
        Coupon coupon=couponRepository.findByCode(code);
        if(coupon==null)
        {
            throw new Exception("Coupon not found");
        }
        Cart cart=cartRepository.findByUserId(user.getId());

        int discountedPrice = (int) ((cart.getTotalSellingPrice()*coupon.getDiscountPercentage())/100);
        cart.setTotalSellingPrice(cart.getTotalSellingPrice()+discountedPrice);
        cart.setCouponCode(null);

        return cartRepository.save(cart);

    }

    @Override
    public Coupon findCouponbyId(Long id) throws Exception {
        return couponRepository.findById(id).orElseThrow(()->new Exception("Coupon not found"));
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")
    public Coupon createCoupon(Coupon coupon)
    {
        return couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> findallCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")
    public void deleteCouponbyId(Long id) throws Exception {
        findCouponbyId(id);
        couponRepository.deleteById(id);
    }
}
