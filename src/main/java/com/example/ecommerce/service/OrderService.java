package com.example.ecommerce.service;

import com.example.ecommerce.domain.OrderStatus;
import com.example.ecommerce.model.*;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<Order> createOrder(User user, Address shippingAddress, Cart cart) throws Exception;

    Order findOrderById(long id) throws Exception;

    List<Order> usersOrderHistory(Long userId) throws Exception;

    List<Order> sellersOrder(Long sellerId) throws Exception;

    Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;

    Order cancelOrder(Long orderId,User user) throws Exception;

    OrderItem findById(Long id) throws Exception;
}
