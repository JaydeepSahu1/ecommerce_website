package com.example.ecommerce.service.impl;

import com.example.ecommerce.domain.PaymentOrderStatus;
import com.example.ecommerce.domain.PaymentStatus;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.PaymentOrder;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.PaymentOrderRepository;
import com.example.ecommerce.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService
{
    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;

    private String apiKey="apiKey";
    private String secretKey="secretKey";
    private String stripesSecretKey="stripesSecretKey";

    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders)
    {
        Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setOrders(orders);
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);

        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {
        return paymentOrderRepository.findById(orderId).orElseThrow(()->
                new Exception("payment order not found"));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception {
        PaymentOrder paymentOrder=paymentOrderRepository.findByPaymentLinkId(orderId);
        if(paymentOrder!=null)
        {
            throw new Exception("Payment order not Found with provided payment link id");
        }
        return null;
    }

    @Override
    public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING))
        {
            RazorpayClient razorpay=new RazorpayClient(apiKey,secretKey);

            Payment payment=razorpay.payments.fetch(paymentId);

            String status=payment.get("status");

            if (status.equals("captured"))
            {
                Set<Order> orders=paymentOrder.getOrders();
                for(Order order:orders)
                {
                    order.setPaymentStates(PaymentStatus.COMPLETED);
                    orderRepository.save(order);
                }
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;
            }
            paymentOrder.setStatus(PaymentOrderStatus.FAILED);
            paymentOrderRepository.save(paymentOrder);
            return false;
        }
        return false;
    }

    @Override
    public PaymentLink createRazorPaymentLink(User user, Long amount, Long orderid)
    {
        amount=amount*100;

        try{
            RazorpayClient razorpay=new RazorpayClient(apiKey,secretKey);

            JSONObject paymentLinkRequest=new JSONObject();
            paymentLinkRequest.put("amount",amount);
            paymentLinkRequest.put("currency","INR");

            JSONObject customer=new JSONObject();
            customer.put("name",user.getFullName());
            customer.put("email",user.getEmail());
            paymentLinkRequest.put("customer",customer);

            JSONObject notify=new JSONObject();
            notify.put("email","true");
            paymentLinkRequest.put("notify",notify);

            paymentLinkRequest.put("callback_url","http://localhost:3000/payment-success/"+ orderid);
            paymentLinkRequest.put("callback_method","get");

            PaymentLink paymentLink=razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkUrl=paymentLink.get("short_url");
            String paymentLinkId=paymentLink.get("id");

            return paymentLink;

        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderid) throws StripeException {
        Stripe.apiKey=stripesSecretKey;

        SessionCreateParams params=SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/"+orderid)
                .setCancelUrl("http://localhost:3000/payment-cancel/"+orderid)
                .addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L).
                        setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("USD")
                                .setUnitAmount(amount*100)
                                .setProductData(SessionCreateParams
                                        .LineItem.PriceData.ProductData
                                        .builder().setName("Mamta Bazar Payment")
                                        .build()
                                ).build()
                        ).build()
                ).build();

        Session session=Session.create(params);

        return session.getUrl();
    }
}
