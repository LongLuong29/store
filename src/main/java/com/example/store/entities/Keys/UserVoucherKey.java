package com.example.store.entities.Keys;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserVoucherKey implements Serializable {
  private Long user;
  private Long voucher;

  @Override
  public int hashCode() {
    return Objects.hash(getVoucher(), getUser());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof UserVoucherKey)) return false;
    UserVoucherKey that = (UserVoucherKey) obj;
    return getUser().equals(that.user) && voucher.equals(that.voucher);
  }
}
