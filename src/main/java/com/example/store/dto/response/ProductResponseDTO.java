package com.example.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private int inventory;
    private BigDecimal price;
    private double rate;
    private String description;
    private boolean deleted;
    private String thumbnail;
    private String[] images;

    private String category;
    private String groupProduct;
    private String brand;

}
