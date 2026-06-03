package com.example.ecommerce.service.impl;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Review;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.ReviewRepository;
import com.example.ecommerce.request.CreateReviewRequest;
import com.example.ecommerce.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;


    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product)
    {
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProductImages(req.getProductImages());

        product.getReviews().add(review);

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewbyProductId(Long productId)
    {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double rating, Long UserId) throws Exception {
        Review review = getReviewById(reviewId);

        if(review.getUser().getId().equals(UserId))
        {
            review.setReviewText(reviewText);
            review.setRating(rating);

            return reviewRepository.save(review);
        }
        throw new Exception("You cannot update this review");
    }

    @Override
    public void deleteReview(Long reviewId, Long UserId) throws Exception {
        Review review = getReviewById(reviewId);

        if(review.getUser().getId().equals(UserId)){
            throw new Exception("you cannot delete this review");
        }
        reviewRepository.delete(review);
    }

    @Override
    public Review getReviewById(Long reviewId) throws Exception {
        return reviewRepository.findById(reviewId).orElseThrow(()->new Exception("Review not found"));
    }
}
