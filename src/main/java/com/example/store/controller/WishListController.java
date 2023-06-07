package com.example.store.controller;

import com.example.store.dto.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/wishlist")
public class WishListController {
    @Autowired private WishListController wishListController;

    @GetMapping(value = "/user")
    public ResponseEntity<?> getUserWishList(@RequestParam(name = "userId") Long userId){
        return wishListController.getUserWishList(userId);
    }
    @PostMapping(value = "/user")
    public ResponseEntity<ResponseObject> createUserWishList(@RequestParam(value = "userId") Long userId,
                                                             @RequestParam(value = "productId") Long productId){
        return wishListController.createUserWishList(userId, productId);
    }
    @DeleteMapping(value = "/user")
    public ResponseEntity<ResponseObject> deleteUserWishList(@RequestParam(value = "userId") Long userId,
                                                                 @RequestParam(value = "productId") Long productId){
        return wishListController.deleteUserWishList(userId,productId);
    }



}
