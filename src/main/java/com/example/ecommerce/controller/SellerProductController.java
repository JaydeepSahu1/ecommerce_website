package com.example.ecommerce.controller;

import com.example.ecommerce.Exceptions.ProductException;
import com.example.ecommerce.Exceptions.SellerException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.request.CreateProductRequeat;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers/products")
public class SellerProductController {

    private final ProductService productService;
    private final SellerService sellerService;

    @GetMapping()
    public ResponseEntity<List<Product>> getProductBySellerId(
                @RequestHeader("Authorization") String jwt ) throws Exception
    {
        Seller seller=sellerService.getSellerProfile(jwt);

        List<Product> products =productService.getProductBySellerId(seller.getId());
        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequeat request,
                                                 @RequestHeader("Authorization") String jwt) throws Exception
    {
        Seller seller=sellerService.getSellerProfile(jwt);

        Product product=productService.createProduct(request,seller);

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("productId") Long productId) throws Exception
    {
        try{
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(ProductException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                 @RequestBody Product product)
    {
        try {
            Product updateProduct = productService.updateProduct(productId,product);
            return new ResponseEntity<>(updateProduct, HttpStatus.OK);
        }
        catch(ProductException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
