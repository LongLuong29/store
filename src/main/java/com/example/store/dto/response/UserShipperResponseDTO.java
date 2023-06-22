package com.example.store.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class UserShipperResponseDTO {
    private Long id;
    private String name;
    private String phone;
}
