package com.example.store.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RankResponseDTO {
    private Long id;
    private String name;
    private double discount;
    private String color;
    private String description;
}
