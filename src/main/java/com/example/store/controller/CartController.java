package com.example.store.controller;

import com.example.store.dto.response.ResponseObject;
import com.example.store.services.CartProductService;
import com.example.store.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/cart")
public class CartController {
  @Autowired private CartService cartService;
  @Autowired private CartProductService cartProductService;

  @GetMapping(value = "/user")
  public ResponseEntity<?> getCartByUser(@RequestParam(name = "userId") Long userId){
    return cartService.getCartByUser(userId);
  }


  //Cart-product
  @PostMapping(value = "/product")
  public ResponseEntity<ResponseObject> addProductToCart(@RequestParam(name = "cartId") Long cartId,
      @RequestParam(name = "productId") Long productId, @RequestParam(name = "amount") int amount){
    return cartProductService.addProductToCart(cartId, productId, amount);
  }

  @DeleteMapping(value = "/product")
  public ResponseEntity<ResponseObject> deteteProductToCart(@RequestParam(name = "cartId") Long cartId,
      @RequestParam(name = "productId") Long productId){
    return cartProductService.deleteProductToCart(cartId, productId);
  }

  @GetMapping(value = "/product")
  public ResponseEntity<?> getProductInCart(@RequestParam(name = "cartId") Long cartId){
    return cartProductService.getProductToCart(cartId);
  }
}
