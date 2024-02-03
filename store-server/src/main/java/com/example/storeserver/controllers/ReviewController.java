package com.example.storeserver.controllers;

import com.example.storeserver.dto.ReviewDTO;
import com.example.storeserver.entity.Review;
import com.example.storeserver.facade.ReviewFacade;
import com.example.storeserver.payload.response.MessageResponse;
import com.example.storeserver.services.ReviewService;
import com.example.storeserver.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("api/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewFacade reviewFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public ReviewController(ReviewService reviewService, ReviewFacade reviewFacade, ResponseErrorValidation responseErrorValidation) {
        this.reviewService = reviewService;
        this.reviewFacade = reviewFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/{productId}/create")
    public ResponseEntity<Object> createReview(@Valid @RequestBody ReviewDTO reviewDTO,
                                               @PathVariable("productId") String productId,
                                               BindingResult bindingResult,
                                               Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Review review = reviewService.createReview(Long.parseLong(productId), reviewDTO, principal);
        ReviewDTO createdReview = reviewFacade.reviewToReviewDTO(review);

        return new ResponseEntity<>(createdReview, HttpStatus.OK);
    }

    @GetMapping("/{productId}/all")
    public ResponseEntity<List<ReviewDTO>> getAllReviewForProduct(@PathVariable("productId") String productId) {
        List<Review> reviews = reviewService.getAllReviewForProductById(Long.parseLong(productId));
        List<ReviewDTO> reviewDTOList = reviews.stream().map(reviewFacade::reviewToReviewDTO).collect(Collectors.toList());

        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}/delete")
    public ResponseEntity<MessageResponse> deleteReviewById(@PathVariable("reviewId") String reviewId) {
        reviewService.deleteReviewById(Long.parseLong(reviewId));

        return new ResponseEntity<>(new MessageResponse("Review was deleted"), HttpStatus.OK);
    }
}
