package com.example.ecommerce.service;

import com.example.ecommerce.Exceptions.ProductException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.request.CreateProductRequeat;
import org.springframework.data.domain.Page;


import java.util.List;

public interface ProductService {

    public Product createProduct(CreateProductRequeat req, Seller seller);

    public void deleteProduct(Long productId) throws ProductException;

    public Product updateProduct(Long productId, Product product) throws ProductException;

    public Product findProductbyId(Long productId) throws ProductException;

    public List<Product> searchProducts(String query);

    public Page<Product> getAllProducts(
            String Category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber
            );

    List<Product> getProductBySellerId(Long sellerId);


}
