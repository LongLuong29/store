package com.example.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
