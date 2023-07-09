package com.example.store.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductResponseDTO {
    private Long id;
    private String name;
    private int inventory;
    private BigDecimal price;
    private double rate;
    private String description;
    private boolean deleted;
    private boolean forSale;
    private String thumbnail;
    private String[] images;

    private BigDecimal discountPrice;
    private double discountPercent;

    private Long categoryId;
    private String category;
    private Long groupProductId;
    private String groupProduct;
    private Long brandId;
    private String brand;

}
