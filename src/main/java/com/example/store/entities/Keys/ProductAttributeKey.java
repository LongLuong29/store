package com.example.store.entities.Keys;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeKey implements Serializable {
    private Long attribute;
    private Long product;
    @Override
    public int hashCode() {
        return Objects.hash(getAttribute(), getProduct());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ProductAttributeKey)) return false;
        ProductAttributeKey that = (ProductAttributeKey) obj;
        return getAttribute().equals(that.attribute) && getProduct().equals(that.product);
    }
}
