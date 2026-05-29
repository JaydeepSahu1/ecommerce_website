package com.example.ecommerce.service.impl;

import com.example.ecommerce.domain.OrderStatus;
import com.example.ecommerce.model.Address;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {


    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) throws Exception {
        return Set.of();
    }

    @Override
    public Order findOrderById(long id) throws Exception {
        return null;
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) throws Exception {
        return List.of();
    }

    @Override
    public List<Order> sellersOrder(Long sellerId) throws Exception {
        return List.of();
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        return null;
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        return null;
    }
}
