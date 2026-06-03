package com.example.ecommerce.controller;

import com.example.ecommerce.model.*;
import com.example.ecommerce.response.ApiResponse;
import com.example.ecommerce.response.PaymentLinkResponse;
import com.example.ecommerce.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController
{
    private final PaymentService paymentService;
    private final UserService userService;
    private final SellerService sellerService;
    private final OrderService orderService;
    private final SellerReportService sellerReportService;
    private final TransactionService transactionService;

    @GetMapping("/api/payment/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader("Authorization") String jwt) throws Exception
    {

        User user = userService.findUserbyJwtToken(jwt);

        PaymentLinkResponse paymentResponse;

        PaymentOrder paymentOrder=paymentService.getPaymentOrderByPaymentId(paymentId);

        boolean paymentSuccess = paymentService.ProceedPaymentOrder(
                paymentOrder,
                paymentId,
                paymentLinkId
        );

        if(paymentSuccess)
        {
            for(Order order:paymentOrder.getOrders())
            {
                transactionService.createTransaction(order);
                Seller seller=sellerService.getSellerById(order.getSellerId());
                SellerReport report =sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders());
                report.setTotalEarnings(report.getTotalEarnings()+order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales()+order.getOrderItems().size());

                sellerReportService.updateSellerReport(report);
            }
        }

        ApiResponse res = new ApiResponse();
        res.setMessage("Payment Success");

        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }
}
