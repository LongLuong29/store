package com.example.store.entities.Keys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderProductKey implements Serializable {
    private Long product;
    private Long order;

    @Override
    public int hashCode() {
        return Objects.hash(getOrder(), getProduct());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OrderProductKey)) return false;
        OrderProductKey that = (OrderProductKey) obj;
        return getOrder().equals(that.order) && getProduct().equals(that.product);
    }
}
