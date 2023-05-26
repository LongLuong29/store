package com.example.store.repositories;

import com.example.store.entities.Cart;
import com.example.store.entities.CartProduct;
import com.example.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    List<CartProduct> findCartProductByCart(Cart cart);

    Optional<CartProduct> findCartProductByCartAndProduct(Cart cart, Product product);
}
