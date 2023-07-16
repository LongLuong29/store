package com.example.store.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VoucherRequestDTO {
    @NotNull(message = "Voucher title is required")
    @Column(length = 100)
    private String title;

    @NotNull(message = "Discount percent is required")
    @Min(value = 1, message = "Percent must be greater 1")
    @Max(value = 99, message = "Percent must smaller 99")
    private double percent;
    private String description;
    private String thumbnail;
    private int quantity;
    private BigDecimal upTo;
    private BigDecimal minSpend;

    private Long voucherType;

    @Size(max = 10, min = 5, message = "Invalid code size")
    private String code;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
}
