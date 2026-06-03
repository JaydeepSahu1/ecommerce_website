package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Review;
import com.example.ecommerce.model.User;
import com.example.ecommerce.request.CreateReviewRequest;
import com.example.ecommerce.response.ApiResponse;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.ReviewService;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) throws Exception
    {
        List<Review> reviews = reviewService.getReviewbyProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<Review> writeReview(
            @RequestBody CreateReviewRequest req,
            @PathVariable Long productId,
            @RequestHeader ("Authorization") String jwt
    ) throws Exception {
        User user=userService.findUserbyJwtToken(jwt);
        Product product=productService.findProductbyId(productId);

        Review review=reviewService.createReview(req,user,product);
        return ResponseEntity.ok(review);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @RequestBody CreateReviewRequest req,
            @PathVariable Long reviewId,
            @RequestHeader ("Authorization") String jwt
    ) throws Exception {
        User user=userService.findUserbyJwtToken(jwt);

        Review review=reviewService.updateReview(
                reviewId,
                req.getReviewText(),
                req.getReviewRating(),
                user.getId()
        );
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader ("Authorization") String jwt
    ) throws Exception {
        User user=userService.findUserbyJwtToken(jwt);

        reviewService.deleteReview(reviewId,user.getId());

        ApiResponse res=new ApiResponse();

        res.setMessage("Review deleted successfully");

        return ResponseEntity.ok(res);
    }
}
