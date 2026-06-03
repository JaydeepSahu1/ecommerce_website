package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.PaymentOrder;
import com.example.ecommerce.model.User;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

import java.util.Set;

public interface PaymentService
{
    PaymentOrder createOrder(User user, Set<Order> orders);
    PaymentOrder getPaymentOrderById(Long orderId) throws Exception;
    PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception;
    Boolean ProceedPaymentOrder(PaymentOrder paymentOrder,String paymentID,String paymentLinkId) throws RazorpayException;
    PaymentLink createRazorPaymentLink(User user,Long amount,Long orderid);
    String createStripePaymentLink(User user,Long amount,Long orderid) throws StripeException;
}
