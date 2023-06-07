package com.example.store.services;

import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.User;
import org.springframework.http.ResponseEntity;

public interface WishListService {
    ResponseEntity<?> getUserWishList(Long userId);
    ResponseEntity<ResponseObject> createUserWishList(Long userId, Long productId);
    ResponseEntity<ResponseObject> deleteUserWishList(Long userId, Long productId);
}
