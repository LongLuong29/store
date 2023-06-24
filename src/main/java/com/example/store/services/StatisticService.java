package com.example.store.services;

import com.example.store.dto.response.OrderProductResponseDTO;
import com.example.store.entities.OrderProduct;
import com.example.store.entities.Product;
import com.example.store.models.IProductQuantity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface StatisticService {

    BigDecimal totalRevenue(Date sinceDay, Date toDay);

    BigDecimal totalSuccessRevenue(Date sinceDay, Date toDay);

    BigDecimal totalProgressRevenue(Date sinceDay, Date toDay);

    BigDecimal totalCancelRevenue(Date sinceDay, Date toDay);

    int totalOrdered();

    List<IProductQuantity> findTopProduct();

    List<Product> topSellerProducts();

    Page<IProductQuantity> numberProductsSold(Pageable pageable);
}
