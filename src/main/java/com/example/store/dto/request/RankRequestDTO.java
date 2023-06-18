package com.example.store.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RankRequestDTO {
    private String name;
    private double discount;
    private String color;
    private String description;
}
