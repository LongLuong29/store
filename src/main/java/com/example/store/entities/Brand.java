package com.example.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="tbl_brand")

public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, unique = true)
    @NotNull(message = "Brand name is required")
    private String name;

    private String image;

    private boolean status;

    // tự tạo ngày giờ hiện tại khi 1 người create / update xuống database
    @CreationTimestamp
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;

    /*

    @CreatedBy
    private User user;

    @LastModifiedBy
    private User user;
     */
}
