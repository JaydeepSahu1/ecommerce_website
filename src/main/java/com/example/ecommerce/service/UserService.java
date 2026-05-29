package com.example.ecommerce.service;

import com.example.ecommerce.model.User;

public interface UserService
{
    public User findUserbyJwtToken(String jwt) throws Exception;
    public User findUserbyEmail(String email) throws Exception;
}
