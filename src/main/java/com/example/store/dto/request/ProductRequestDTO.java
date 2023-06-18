package com.example.store.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductRequestDTO {
    @Size(message = "Size is invalid.", max = 200, min = 1)
    private String name;
    @Min(value = 1, message = "Amount must be greater 1")
    private int inventory;
    @Min(value = 1, message = "Stand cost must be greater 1")
    private BigDecimal price;
    private String description;
    private MultipartFile thumbnail;

    private Long category;
    private Long brand;
    private Long groupProduct;

    private MultipartFile[] images;

}
