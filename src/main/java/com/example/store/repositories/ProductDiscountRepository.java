package com.example.store.repositories;

import com.example.store.entities.Discount;
import com.example.store.entities.Product;
import com.example.store.entities.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {
  Optional<ProductDiscount> findProductDiscountByDiscountAndProduct(Discount discount, Product product);
  List<ProductDiscount> findProductDiscountByProduct(Product product);

}
