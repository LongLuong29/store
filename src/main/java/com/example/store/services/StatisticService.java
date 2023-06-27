package com.example.store.services;

import com.example.store.entities.Order;
import com.example.store.entities.Product;
import com.example.store.models.IProductQuantity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface StatisticService {

    BigDecimal totalRevenue(Date sinceDay, Date toDay);

    BigDecimal totalSuccessRevenue(Date sinceDay, Date toDay);

    BigDecimal totalProgressRevenue(Date sinceDay, Date toDay);

    BigDecimal totalCancelRevenue(Date sinceDay, Date toDay);

    int totalOrdered();

    ResponseEntity<?> findTopProduct();

    ResponseEntity<?> findSoldProductAmount(Pageable pageable);

    int countOrderAmountByOrderStatus(String orderStatus);

    List<BigDecimal> totalRevenueIn7Days();

    ResponseEntity<?> find5RecentOrder();

    List<Integer> countTotalNewCustomer7Days();
}
