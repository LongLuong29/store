package com.example.store.repositories;

import com.example.store.entities.Product;
import com.example.store.entities.User;
import com.example.store.entities.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    Optional<WishList> findWishListByUserAndProduct(User user, Product product);
    List<WishList> findWishListByUser(User user);
}
