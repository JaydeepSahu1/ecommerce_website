package com.example.ecommerce.controller;

import com.example.ecommerce.domain.PaymentMethod;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.response.PaymentLinkResponse;
import com.example.ecommerce.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController
{
    private final UserService userService;
    private final OrderService orderService;
    private final CartService cartService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;


    @PostMapping()
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(
            @RequestBody Address shippingAddress,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization") String jwt)
        throws Exception {

        User user =userService.findUserbyJwtToken(jwt);
        Cart cart=cartService.findUserCart(user);
        Set<Order> orders =orderService.createOrder(user,shippingAddress,cart);

//      PaymentOrder paymentOrder=paymentService.createOrder(user,orders);

        PaymentLinkResponse res = new PaymentLinkResponse();
//
//        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
//            PaymentLink payment=paymentSe
//        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> userOrderHistoryHandler(
            @RequestHeader("Authorization") String jwt)
            throws Exception
        {
            User user=userService.findUserbyJwtToken(jwt);
            List<Order> orders=orderService.usersOrderHistory(user.getId());

            return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
        }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt)
        throws Exception {

        User user=userService.findUserbyJwtToken(jwt);
        Order orders=orderService.findOrderById(orderId);

        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(
            @PathVariable Long orderItemId,
            @RequestHeader("Authorization") String jwt)
        throws Exception {
        User user = userService.findUserbyJwtToken(jwt);
        OrderItem orderItem = orderService.getOrderItemById(orderItemId);

        return new ResponseEntity<>(orderItem, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt)
        throws Exception
        {
            User user = userService.findUserbyJwtToken(jwt);
            Order order=orderService.cancelOrder(orderId,user);

            Seller sellers = sellerService.getSellerById(user.getId());
            SellerReport report=sellerReportService.getSellerReport(sellers);

            report.setCanceledOrders(report.getCanceledOrders()+1);
            report.setTotalRefunds(report.getTotalRefunds()+order.getTotalSellingPrice());
            sellerReportService.updateSellerReport(report);

            return ResponseEntity.ok(order);

        }

}
