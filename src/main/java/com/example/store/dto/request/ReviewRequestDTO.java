package com.example.store.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewRequestDTO {
    private Long id;
    @NotNull(message = "Feedback content is required")
    private String content;
    private double vote;

    private Long user;
    private Long product;

}
