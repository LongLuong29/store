package com.example.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStatisticResponseDTO {
    private Long id;
    private String name;
    private int quantity;
    private BigDecimal price;
    private int quantitySales;
    private BigDecimal totalPrice;
}
