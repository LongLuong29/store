package com.example.store.entities.Keys;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDiscountKey implements Serializable {
  private Long user;
  private Long discount;

  @Override
  public int hashCode() {
    return Objects.hash(getDiscount(), getUser());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof UserDiscountKey)) return false;
    UserDiscountKey that = (UserDiscountKey) obj;
    return getUser().equals(that.user) && discount.equals(that.discount);
  }
}
