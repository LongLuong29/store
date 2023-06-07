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
    private String color;
    private String description;
}
