package com.example.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private Long groupProduct;
    private boolean status;
}
