package com.example.store.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WishListResponseDTO {
    private Long userId;
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
}
