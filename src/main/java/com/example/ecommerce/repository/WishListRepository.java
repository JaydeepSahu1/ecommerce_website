package com.example.ecommerce.repository;

import com.example.ecommerce.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList,Long> {

    WishList findbyUserId(Long userId);

}
