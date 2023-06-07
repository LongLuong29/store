package com.example.store.entities;


import com.example.store.entities.Keys.UserVoucherKey;
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
@Table(name = "tbl_user_voucher")
@IdClass(UserVoucherKey.class)
public class UserVoucher {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "voucher_id")
  private Voucher voucher;
}
