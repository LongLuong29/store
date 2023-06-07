package com.example.store.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class StatisticResponseDTO {
    private String times;
    private double value;
}
