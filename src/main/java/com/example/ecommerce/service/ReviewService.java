package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Review;
import com.example.ecommerce.model.User;
import com.example.ecommerce.request.CreateReviewRequest;


import java.util.List;

public interface ReviewService {

    Review createReview(CreateReviewRequest req, User user, Product product);

    List<Review> getReviewbyProductId(Long productId);

    Review updateReview(Long reviewId,String reviewText,double rating,Long UserId) throws Exception;

    void deleteReview(Long reviewId,Long UserId) throws Exception;

    Review getReviewById(Long reviewId) throws Exception;
}
