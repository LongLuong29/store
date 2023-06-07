package com.example.store.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private Long groupProduct;
    private boolean status;
}
