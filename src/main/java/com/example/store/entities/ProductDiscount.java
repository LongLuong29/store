package com.example.store.entities;

import com.example.store.entities.Keys.ProductDiscountKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_product_discount")
@IdClass(ProductDiscountKey.class)
public class ProductDiscount {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  private Product product;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "discount_id")
  private Discount discount;
}
