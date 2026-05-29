package com.example.ecommerce.service.impl;

import com.example.ecommerce.config.JwtProvider;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserbyJwtToken(String jwt) throws Exception
    {
        String email=jwtProvider.getEmailFromJwtToken(jwt);

        User user = this.findUserbyEmail(email);

        if(user==null){
            throw new Exception("user not found with email - "+email);
        }
        return user;
    }

    @Override
    public User findUserbyEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);

        if(user==null){
            throw new Exception("user not found with email - "+email);
        }
        return user;
    }
}
