package com.example.store.entities.Keys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishListKey implements Serializable {
    private Long user;
    private Long product;

    @Override
    public int hashCode() { return Objects.hash(user, product); }

    @Override
    public boolean equals(Object o) {
        if (this == o)  return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WishListKey that = (WishListKey) o;
        return Objects.equals(user, that.user) && Objects.equals(product,
                that.product);
    }

}
