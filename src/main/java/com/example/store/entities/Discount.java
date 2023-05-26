package com.example.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "tbl_discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45)
    @NotNull(message = "Discount title is required")
    private String title;

    private String description;

    @NotNull(message = "Discount code is required")
    @Min(value = 1, message = "Percent must be greater 1")
    @Max(value = 99, message = "Percent must smaller 99")
    private double percent;

    private boolean status;

    @Size(max = 14, min = 5, message = "Invalid code size")
    private String code;

    @CreationTimestamp
    private Date createdDate;

    @UpdateTimestamp
    private Date updatedDate;

}
