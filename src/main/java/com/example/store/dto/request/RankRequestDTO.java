package com.example.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankRequestDTO {
    private String name;
    private double discount;
    private double point;
    private String icon;
    private String color;
    private String description;
}
