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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_image_review")
public class ImageReview {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  @NotNull(message = "Path image product is required")
  private String path;

  @ManyToOne(optional = false)
  @JoinColumn(name = "review_id")
  private Review review;

  // tự tạo ngày giờ hiện tại khi 1 người create / update xuống database
  @CreationTimestamp
  private Date createdDate;
  @UpdateTimestamp
  private Date updatedDate;

}
