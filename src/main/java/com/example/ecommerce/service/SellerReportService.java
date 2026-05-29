package com.example.ecommerce.service;

import com.example.ecommerce.model.Seller;
import com.example.ecommerce.model.SellerReport;

public interface SellerReportService {

    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);
}
