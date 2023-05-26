package com.example.store.services.implement;

import com.example.store.dto.response.CartProductResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.Cart;
import com.example.store.entities.CartProduct;
import com.example.store.entities.Product;
import com.example.store.exceptions.InvalidValueException;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.CartProductMapper;
import com.example.store.repositories.CartProductRepository;
import com.example.store.repositories.CartRepository;
import com.example.store.repositories.ProductRepository;
import com.example.store.repositories.UserRepository;
import com.example.store.services.CartProductService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartProductServiceImpl implements CartProductService {

    @Autowired private CartProductRepository cartProductRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private ProductRepository productRepository;

    private final CartProductMapper cartProductMapper = Mappers.getMapper(CartProductMapper.class);

    @Override
    public ResponseEntity<ResponseObject> addProductToCart(Long cartId, Long productId, int amount) {
        CartProduct cartProduct = new CartProduct();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find cart with ID = " + cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));

        if(amount > product.getInventory()){
            throw new InvalidValueException("Amount product add to cart must be less than amount product exists");
        }
        Optional<CartProduct> getCartProduct =cartProductRepository.findCartProductByCartAndProduct(cart,product);
        // cart is exist
        if(getCartProduct.isPresent()){
            if(amount < 0 ){throw new InvalidValueException("Amount product must greater than 1");}
            cartProduct = getCartProduct.get();
            int newAmount = getCartProduct.get().getAmount() + amount;
            cartProduct.setAmount(newAmount);
        }else {
            cartProduct.setCart(cart);
            cartProduct.setProduct(product);
            cartProduct.setAmount(amount);
        }
        CartProduct cartProductSaved = cartProductRepository.save(cartProduct);
        CartProductResponseDTO cartProductResponseDTO = cartProductMapper.cartProductToCartProductResponseDTO(cartProductSaved);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Add product to cart success!", cartProductResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteProductToCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find cart with ID = " + cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
        CartProduct cartProduct = cartProductRepository.findCartProductByCartAndProduct(cart,product)
            .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId + " and cart ID = " + cartId));
        cartProductRepository.delete(cartProduct);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Delete product in cart success!"));
    }

    @Override
    public ResponseEntity<?> getProductToCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find cart with ID = " + cartId));

        List<CartProduct> cartProductList = cartProductRepository.findCartProductByCart(cart);
        List<CartProductResponseDTO> cartProductResponseDTOList = new ArrayList<>();
        for (CartProduct cartProduct : cartProductList){
            CartProductResponseDTO cartProductResponseDTO = cartProductMapper.cartProductToCartProductResponseDTO(cartProduct);
            cartProductResponseDTOList.add(cartProductResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(cartProductResponseDTOList);
    }

}
