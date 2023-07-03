package com.example.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderCode;

    private String status;

    @NotNull(message = "Order total price is required")
    private BigDecimal totalPrice;
    private BigDecimal shippingFee;
    private BigDecimal finalPrice;
//    private BigDecimal orderTotal;
    @Column(length = 333)
    private String note;

    @NotNull(message = "Payment method is required")
    private String paymentMethod;

    @Temporal(TemporalType.DATE)
    private Date orderedDate;
    @Temporal(TemporalType.DATE)
    private Date doneDate;
    @Temporal(TemporalType.DATE)
    private Date deliveredDate;
    @Temporal(TemporalType.DATE)
    private Date paidDate;
    //foreign key
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "shipper_id")
//    private User shipper;

    //cre - edi
    @CreationTimestamp
    private Date createdDate;

    @UpdateTimestamp
    private Date updatedDate;

}
