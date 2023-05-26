package com.example.store.repositories;

import com.example.store.entities.Attribute;
import com.example.store.entities.ProductAttribute;
import com.example.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
  List<ProductAttribute> findProductAttributeByAttribute(Attribute attribute);
  Optional<ProductAttribute> findProductAttributeByAttributeAndProduct(Attribute attribute, Product product);
  List<ProductAttribute> findProductAttributeByProduct(Product product);
}
