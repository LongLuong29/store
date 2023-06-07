package com.example.store.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartResponseDTO {
    private Long userId;
    private Long cartId;
    private int amount;
}
