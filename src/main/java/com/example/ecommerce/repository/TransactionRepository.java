package com.example.ecommerce.repository;

import com.example.ecommerce.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer>
{
    List<Transaction> findBySellerId(Long sellerid);
}
