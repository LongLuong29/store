package com.example.store.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupProductRequestDTO {
    private Long id;
    private String name;
}
