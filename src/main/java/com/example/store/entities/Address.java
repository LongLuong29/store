package com.example.store.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Apartment number is required")
    private String apartmentNumber;

    @NotNull(message = "Ward is required")
    private String ward;

    @NotNull(message = "District is required")
    private String district;

    @NotNull(message = "Province is required")
    private String province;

    // tự tạo ngày giờ hiện tại khi 1 người create / update xuống database
    @CreationTimestamp
    private Date createdDate;

    @UpdateTimestamp
    private Date updatedDate;

}
