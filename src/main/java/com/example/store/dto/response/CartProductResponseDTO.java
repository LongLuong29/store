package com.example.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartProductResponseDTO {
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private Long cartId;
    private int amount;
}
