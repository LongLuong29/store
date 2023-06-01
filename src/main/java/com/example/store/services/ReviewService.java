package com.example.store.services;


import com.example.store.dto.response.ResponseObject;
import com.example.store.dto.request.ReviewRequestDTO;
import com.example.store.dto.response.ReviewResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ReviewService {
    ResponseEntity<?> getAllReviewOnTrading(Pageable pageable);

    ResponseEntity<?> getAllReviewByProduct(Pageable pageable, Long productId);

    ResponseEntity<ResponseObject> createReview(ReviewRequestDTO reviewRequestDTO);

    ResponseEntity<ResponseObject> updateReview(ReviewRequestDTO reviewRequestDTO);

    ResponseEntity<ResponseObject> deleteReview(Long reviewId);

    ReviewResponseDTO getReviewById(Long reviewId);
}
