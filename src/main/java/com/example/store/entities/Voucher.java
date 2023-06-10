package com.example.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "tbl_voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Voucher title is required")
    @Column(length = 100)
    private String title;

    @NotNull(message = "Discount percent is required")
    @Min(value = 1, message = "Percent must be greater than 1")
    @Max(value = 100, message = "Percent must smaller than 100")
    private double percent;
    private boolean status;
    private String description;
    private String thumbnail;
    @NotNull(message = "Voucher has to have a limit discount amount")
    private BigDecimal upTo;
    private BigDecimal minSpend;

    @ManyToOne(optional = true)
    @JoinColumn(name = "voucherType_id")
    private VoucherType voucherType;

    @Size(max = 10, min = 5, message = "Invalid code size")
    private String code;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @CreationTimestamp
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;

}
