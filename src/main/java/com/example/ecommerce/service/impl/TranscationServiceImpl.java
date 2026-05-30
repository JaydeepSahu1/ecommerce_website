package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.model.Transaction;
import com.example.ecommerce.repository.SellerRepository;
import com.example.ecommerce.repository.TransactionRepository;
import com.example.ecommerce.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranscationServiceImpl implements TransactionService
{
    private TransactionRepository transactionRepository;
    private SellerRepository sellerRepository;

    @Override
    public Transaction createTransaction(Order order) {

        Seller seller=sellerRepository.findById(order.getSellerId()).get();

        Transaction transaction=new Transaction();
        transaction.setSeller(seller);
        transaction.setOrder(order);
        transaction.setCustomer(order.getUser());

        return transactionRepository.save(transaction);

    }

    @Override
    public List<Transaction> getTransactionsBySellerId(Seller seller) {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }
}
