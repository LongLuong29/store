package com.example.store.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="tbl_banner")
public class  Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @NotNull(message = "Photo url is required")
    private String photoUrl;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    private boolean status;

    // tự tạo ngày giờ hiện tại khi 1 người create / update xuống database
    @CreationTimestamp
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;

}
