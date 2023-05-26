package com.example.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="tbl_rank")
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Rank name is required")
    private String name;

    private double discount;

    @NotNull(message = "Point is required")
    private double point;

    @Column(name = "Rank icon")
    private String icon;

    @NotNull(message = "Color is required")
    private String color;

    @Column(name = "Rank description")
    private String description;

    @UpdateTimestamp
    private Date updatedDate;
}
