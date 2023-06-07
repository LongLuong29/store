package com.example.store.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupProductResponseDTO {
    private Long id;
    private String name;
    private boolean status;
}
