package com.example.store.repositories;

import com.example.store.entities.ImageReview;
import com.example.store.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageReviewRepository extends JpaRepository<ImageReview, Long> {
    List<ImageReview> findImageReviewByReview(Review review);

}
