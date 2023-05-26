package com.example.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RankResponseDTO {
    private Long id;
    private String name;
    private double discount;
    private double point;
    private String icon;
    private String color;
    private String description;
}
