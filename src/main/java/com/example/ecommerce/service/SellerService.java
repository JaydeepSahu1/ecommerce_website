package com.example.ecommerce.service;

import com.example.ecommerce.Exceptions.SellerException;
import com.example.ecommerce.domain.AccountStatus;
import com.example.ecommerce.model.Seller;

import java.util.List;

public interface SellerService {

    Seller getSellerProfile(String jwt) throws Exception;
    Seller createSeller(Seller seller) throws Exception;
    Seller getSellerById(Long id) throws SellerException;
    Seller getSellerByEmail(String email) throws Exception;
    List<Seller> getAllSellers(AccountStatus status);
    Seller updateSeller(long id , Seller seller) throws Exception;
    void deleteSeller(long id) throws Exception;

    Seller verifyEmail(String email, String otp) throws Exception;
    Seller updateSellerAccountStatus(long id, AccountStatus status) throws Exception;



}
