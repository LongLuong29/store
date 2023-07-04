package com.example.store.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile[] images;

    private Long user;
    private Long product;

}
