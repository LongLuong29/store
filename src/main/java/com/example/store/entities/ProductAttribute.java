package com.example.store.entities;

import com.example.store.entities.Keys.ProductAttributeKey;
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
@Table(name = "tbl_product_attribute")
@IdClass(ProductAttributeKey.class)
public class ProductAttribute {
    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;
}

