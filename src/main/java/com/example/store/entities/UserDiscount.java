package com.example.store.entities;

import com.example.store.entities.Keys.UserDiscountKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_user_discount")
@IdClass(UserDiscountKey.class)
public class UserDiscount {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "discount_id")
  private Discount discount;
}
