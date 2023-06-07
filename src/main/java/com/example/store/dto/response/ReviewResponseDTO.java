package com.example.store.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewResponseDTO {
    private Long id;
    private String content;
    private double vote;
    private Long user;
    private String userName;
    private Long product;
    private String productName;
    private String productThumbnail;

    private Date createDate;
    private Date updateDate;
}
