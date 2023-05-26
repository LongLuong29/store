package com.example.store.entities.Keys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDiscountKey implements Serializable {
  private Long product;
  private Long discount;

  @Override
  public int hashCode() {
    return Objects.hash(getDiscount(), getProduct());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof ProductDiscountKey)) return false;
    ProductDiscountKey that = (ProductDiscountKey) obj;
    return getProduct().equals(that.product) && discount.equals(that.discount);
  }
}
