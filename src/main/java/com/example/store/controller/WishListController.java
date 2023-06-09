package com.example.store.controller;

import com.example.store.dto.response.ResponseObject;
import com.example.store.services.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/wishlist")
public class WishListController {
    @Autowired private WishListService wishListService;

    @GetMapping(value = "/user")
    public ResponseEntity<?> getUserWishList(@RequestParam(name = "userId") Long userId){
        return wishListService.getUserWishList(userId);
    }
    @PostMapping(value = "/user")
    public ResponseEntity<ResponseObject> createUserWishList(@RequestParam(value = "userId") Long userId,
                                                             @RequestParam(value = "productId") Long productId){
        return wishListService.createUserWishList(userId, productId);
    }
    @DeleteMapping(value = "/user")
    public ResponseEntity<ResponseObject> deleteUserWishList(@RequestParam(value = "userId") Long userId,
                                                                 @RequestParam(value = "productId") Long productId){
        return wishListService.deleteUserWishList(userId,productId);
    }



}
