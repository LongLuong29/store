package com.example.store.entities;

import com.example.store.entities.Keys.ProductDiscountKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_product_discount")
@IdClass(ProductDiscountKey.class)
public class ProductDiscount {
  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  private Product product;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "discount_id")
  private Discount discount;

//  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//  @Column(name = "start_date")
//  private Date startDate;
//
//  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//  @Column(name = "end_date")
//  private Date endDate;

  private boolean deleted;

  @CreationTimestamp
  private Date createdDate;

  @UpdateTimestamp
  private Date updatedDate;

}
