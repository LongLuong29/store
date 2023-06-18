package com.example.store.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BrandRequestDTO {
    private String name;
    private String image;
    private String status;
}
