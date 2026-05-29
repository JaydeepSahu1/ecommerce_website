package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>
{
    List<Product> findBySellerId(Long id);

//    @Query("SELECT p FROM Product p WHERE (:query IS NULL OR + LOWER(p.title)" +
//            "LIKE LOWER(CONCAT('%', :query, '%') ) ) " +
//            "OR (:query is null or lower (p.category.name)"+
//            "LIKE LOWER(CONCAT('%', :query, '%') ) )")

    @Query("SELECT p FROM Product p " +
            "WHERE (:query IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :query, '%')) )")

    List<Product> searchProducts(@Param("query") String query);

}
