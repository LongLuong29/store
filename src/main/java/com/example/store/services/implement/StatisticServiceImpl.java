package com.example.store.services.implement;

import com.example.store.entities.Order;
import com.example.store.entities.Product;
import com.example.store.models.IProductQuantity;
import com.example.store.repositories.OrderProductRepository;
import com.example.store.repositories.OrderRepository;
import com.example.store.repositories.ProductRepository;
import com.example.store.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderProductRepository orderProductRepository;

    @Override
    public BigDecimal totalRevenue(Date sinceDay, Date toDay) {
        List<Order> orderList = orderRepository.findOrderByDate(sinceDay, toDay);
        BigDecimal totalRevenue = BigDecimal.valueOf(0);
        for (Order o: orderList){
            totalRevenue = totalRevenue.add(o.getFinalPrice());
        }
        return totalRevenue;
    }

    @Override
    public BigDecimal totalSuccessRevenue(Date sinceDay, Date toDay) {
        List<Order> orderList = orderRepository.findOrderByDate(sinceDay, toDay);
        BigDecimal totalRevenue = BigDecimal.valueOf(0);
        for (Order o: orderList){
            if(o.getStatus().equals("Done")){
                totalRevenue = totalRevenue.add(o.getFinalPrice());
            }
        }
        return totalRevenue;
    }

    @Override
    public BigDecimal totalProgressRevenue(Date sinceDay, Date toDay) {
        List<Order> orderList = orderRepository.findOrderByDate(sinceDay, toDay);
        BigDecimal totalRevenue = BigDecimal.valueOf(0);
        for (Order o: orderList){
            if(!o.getStatus().equals("Done") && !o.getStatus().equals("Cancel")){
                totalRevenue = totalRevenue.add(o.getFinalPrice());
            }
        }
        return totalRevenue;
    }

    @Override
    public BigDecimal totalCancelRevenue(Date sinceDay, Date toDay) {
        List<Order> orderList = orderRepository.findOrderByDate(sinceDay, toDay);
        BigDecimal totalRevenue = BigDecimal.valueOf(0);
        for (Order o: orderList){
            if(o.getStatus().equals("Cancel")){
                totalRevenue = totalRevenue.add(o.getFinalPrice());
            }
        }
        return totalRevenue;
    }

    @Override
    public List<Product> topSellerProducts() {
        return null;
    }

    @Override
    public Page<IProductQuantity> numberProductsSold(Pageable pageable) {
        return null;
    }
}
