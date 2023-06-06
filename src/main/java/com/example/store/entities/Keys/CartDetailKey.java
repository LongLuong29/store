package com.example.store.entities.Keys;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailKey implements Serializable {
    private Long product;
    private Long cart;

    @Override
    public int hashCode() {
        return Objects.hash(getCart(), getProduct());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof CartDetailKey)) return false;
        CartDetailKey that = (CartDetailKey) obj;
        return getCart().equals(that.cart) && getProduct().equals(that.product);
    }
}
