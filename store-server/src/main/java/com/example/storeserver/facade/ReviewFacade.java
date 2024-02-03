package com.example.storeserver.facade;

import com.example.storeserver.dto.ReviewDTO;
import com.example.storeserver.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewFacade {

    public ReviewDTO reviewToReviewDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setMessage(review.getMessage());
        reviewDTO.setUsername(review.getUsername());
        reviewDTO.setUserId(review.getUserId());

        return reviewDTO;
    }

}
