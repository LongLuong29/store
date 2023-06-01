package com.example.store.repositories;

import com.example.store.entities.Review;
import com.example.store.entities.Product;
import com.example.store.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
  Page<Review> findReviewByProduct(Pageable pageable, Product product);

  List<Review> findReviewsByProduct(Product product);

  List<Review> findReviewByUser(User user);
}
