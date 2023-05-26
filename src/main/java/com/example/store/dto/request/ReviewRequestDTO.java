package com.example.store.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDTO {
    private Long id;
    @NotNull(message = "Feedback content is required")
    private String content;
    private double rate;

    private Long user;
    private Long product;

}
