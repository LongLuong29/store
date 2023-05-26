package com.example.store.services.implement;

import com.example.store.dto.response.CartResponseDTO;
import com.example.store.entities.Cart;
import com.example.store.entities.User;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.CartMapper;
import com.example.store.repositories.CartProductRepository;
import com.example.store.repositories.CartRepository;
import com.example.store.repositories.UserRepository;
import com.example.store.services.CartService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CartProductRepository cartProductRepository;

    private final CartMapper cartMapper = Mappers.getMapper(CartMapper.class);
    @Override
    public ResponseEntity<CartResponseDTO> getCartByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
        Cart cart = cartRepository.findCartByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find cart with user ID = " + userId));
        CartResponseDTO cartResponseDTO = cartMapper.cartToCartResponseDTO(cart);
        // tính tổng số lượng sản phẩm có trong giỏ
        int totalProduct = cartProductRepository.findCartProductByCart(cart).size();
        cartResponseDTO.setAmount(totalProduct);
        return ResponseEntity.status(HttpStatus.OK).body(cartResponseDTO);
    }
}
