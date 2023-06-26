package com.example.store.dto.response;

import com.example.store.entities.Product;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductQuantityResponseDTO {
    ProductResponseDTO product;
    int quantity;
}
