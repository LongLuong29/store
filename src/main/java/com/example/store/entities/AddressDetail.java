package com.example.store.entities;

import com.example.store.entities.Keys.AddressDetailKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_address_detail")
@IdClass(AddressDetailKey.class)
public class AddressDetail {
    @Id
    @ManyToOne(fetch = FetchType.LAZY) // => không load lập tức
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    private boolean defaultAddress;
    private boolean deleted;


    // tự tạo ngày giờ hiện tại khi 1 người create / update xuống database
    @CreationTimestamp
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;


}
