package com.example.store.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TopShipperResponseDTO {
    private String shipperName;
    private int orderAmount;
}
