package com.example.store.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_image_product")
public class ImageProduct {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  @NotNull(message = "Path image product is required")
  private String path;

  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  private Product product;

  // tự tạo ngày giờ hiện tại khi 1 người create / update xuống database
  @CreationTimestamp
  private Date createdDate;
  @UpdateTimestamp
  private Date updatedDate;

}
