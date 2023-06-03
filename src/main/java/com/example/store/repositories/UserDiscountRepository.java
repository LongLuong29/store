package com.example.store.repositories;

import com.example.store.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDiscountRepository extends JpaRepository<UserDiscount, Long> {
  Optional<UserDiscount> findUserDiscountByDiscountAndUser(Discount discount, User user);
  List<UserDiscount> findUserDiscountByUser(User user);

}
