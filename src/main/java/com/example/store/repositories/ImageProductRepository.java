package com.example.store.repositories;

import com.example.store.entities.ImageProduct;
import com.example.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageProductRepository extends JpaRepository<ImageProduct, Long> {
  List<ImageProduct> findImageProductByProduct(Product product);

}
