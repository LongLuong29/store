package com.example.store.entities;

import com.example.store.entities.Keys.CartDetailKey;
import com.example.store.entities.Keys.WishListKey;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_wish_list")
@IdClass(WishListKey.class)
public class WishList {
    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @CreationTimestamp
    private Date createdDate;

    @UpdateTimestamp
    private Date updatedDate;
}
