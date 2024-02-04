package com.example.storeserver.services;

import com.example.storeserver.dto.ReviewDTO;
import com.example.storeserver.entity.Customer;
import com.example.storeserver.entity.Product;
import com.example.storeserver.entity.Review;
import com.example.storeserver.exceptions.ProductNotFoundException;
import com.example.storeserver.exceptions.ReviewNotFoundException;
import com.example.storeserver.repositories.CustomerRepository;
import com.example.storeserver.repositories.ProductRepository;
import com.example.storeserver.repositories.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class ReviewService {
    public static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    // api/review/:productId/create
    public Review createReview(Long productId, ReviewDTO reviewDTO, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));
        Review review = new Review();
        review.setUsername(customer.getUsername());
        review.setUserId(customer.getId());
        review.setProduct(product);
        review.setMessage(reviewDTO.getMessage());

        Review resultReview = reviewRepository.save(review);

        product.getReviews().add(review);
        productRepository.save(product);

        LOG.info("Saving Review");
        return resultReview;
    }

    // api/review/:productId/all
    public List<Review> getAllReviewForProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));
        return reviewRepository.findAllByProduct(product);
    }

    // api/review/:reviewId/delete
    public void deleteReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review cannot be found"));

        reviewRepository.delete(review);
    }

    private Customer getCustomerByPrincipal(Principal principal) {
        String username = principal.getName();
        return customerRepository.findCustomerByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
    }

}
