package com.example.store.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductGalleryDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String thumbnail;
    private double discount;
    private double rate;

    private String groupProduct;
    private String category;
    private String brand;
}
