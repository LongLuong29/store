package com.example.store.services.implement;

import com.example.store.dto.response.OrderProductResponseDTO;
import com.example.store.entities.Order;
import com.example.store.entities.OrderProduct;
import com.example.store.entities.Product;
import com.example.store.mapper.OrderProductMapper;
import com.example.store.models.IProductQuantity;
import com.example.store.repositories.OrderProductRepository;
import com.example.store.repositories.OrderRepository;
import com.example.store.repositories.ProductRepository;
import com.example.store.services.StatisticService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderProductRepository orderProductRepository;

    private final OrderProductMapper orderProductMapper = Mappers.getMapper(OrderProductMapper.class);

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
    public int totalOrdered() {
        List<Order> orderList = orderRepository.findAll();
        int amount = orderList.size();
        return amount;
    }

    @Override
    public List<IProductQuantity> findTopProduct() {
        List<IProductQuantity> topSellerProduct = orderProductRepository.findTopProduct();
//        List<OrderProductResponseDTO> orderProductResponseDTOList = new ArrayList<>();
//        for(OrderProduct op: topSellerProduct){
//            OrderProductResponseDTO orderProductResponseDTO = orderProductMapper.orderProductToOrderProductResponseDTO(op);
//            orderProductResponseDTOList.add(orderProductResponseDTO);
//        }
        return topSellerProduct;
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
