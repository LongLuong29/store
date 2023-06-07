package com.example.store.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BrandResponseDTO {
    private Long id;
    private String name;
    private String image;
    private boolean status;
}
