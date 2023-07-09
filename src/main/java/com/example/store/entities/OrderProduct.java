package com.example.store.entities;

import com.example.store.entities.Keys.OrderProductKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_order_product")
@IdClass(OrderProductKey.class)
public class OrderProduct {
    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;
    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private BigDecimal pricePerOne;
    private BigDecimal discountPrice;

    // tự tạo ngày giờ hiện tại khi 1 người create / update xuống database
    @CreationTimestamp
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;

}
