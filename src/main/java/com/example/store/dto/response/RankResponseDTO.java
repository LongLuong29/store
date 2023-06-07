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
    private double point;
    private String icon;
    private String color;
    private String description;
}
