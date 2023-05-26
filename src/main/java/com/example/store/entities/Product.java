package com.example.store.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 45, unique = true)
    @NotNull(message = "Product name is required")
    private String name;
    @Column(name = "thumbnail")
    private String thumbnail;
    @NotNull(message = "Price is required")
    private BigDecimal price;
    private double rate;
    private int inventory;
    private String description;
//    @NotNull(message = "Status is required")
//    private boolean status;
    private boolean deleted;

    // thiết lập khóa ngoại---------------------------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_product_id")
    private GroupProduct groupProduct;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "description_id")
//    private Attribute attribute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private Discount discount;

    // time
    @CreationTimestamp
    private Date createdDate;

    @UpdateTimestamp
    private Date updatedDate;

}
