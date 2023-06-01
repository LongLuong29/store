package com.example.store.controller;

import com.example.store.dto.request.ReviewRequestDTO;
import com.example.store.dto.response.ReviewResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.services.ReviewService;
import com.example.store.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/review")
public class ReviewController {
    @Autowired private ReviewService reviewService;

    @GetMapping(value = "")
    public ResponseEntity<?> getAllReviewOnTrading(
            @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return reviewService.getAllReviewOnTrading(pageable);
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createReview(@ModelAttribute @Valid ReviewRequestDTO reviewRequestDTO) {
        return reviewService.createReview(reviewRequestDTO);
    }

    @DeleteMapping(value = "")
    public ResponseEntity<ResponseObject> deleteReview(@RequestParam(name = "id") Long id) {
        return reviewService.deleteReview(id);
    }

    @GetMapping(value = "/{reivewId}")
    public ReviewResponseDTO getReviewById(@PathVariable(name = "reviewId") Long reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    @GetMapping(value = "/product")
    public ResponseEntity<?> getAllReviewByProduct(
            @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "productId") Long productId
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return reviewService.getAllReviewByProduct(pageable, productId);
    }
}
