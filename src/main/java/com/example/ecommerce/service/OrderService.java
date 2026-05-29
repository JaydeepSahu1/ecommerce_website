package com.example.ecommerce.service;

import com.example.ecommerce.domain.OrderStatus;
import com.example.ecommerce.model.Address;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<Order> createOrder(User user, Address shippingAddress, Cart cart) throws Exception;

    Order findOrderById(long id) throws Exception;

    List<Order> usersOrderHistory(Long userId) throws Exception;

    List<Order> sellersOrder(Long sellerId) throws Exception;

    Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;

    Order cancelOrder(Long orderId,User user) throws Exception;
}
