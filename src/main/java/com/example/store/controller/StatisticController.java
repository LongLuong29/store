package com.example.store.controller;

import com.example.store.dto.response.OrderProductResponseDTO;
import com.example.store.entities.OrderProduct;
import com.example.store.entities.Product;
import com.example.store.models.IProductQuantity;
import com.example.store.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
    @Autowired
    StatisticService statisticService;

    @GetMapping(value = "/total-revenue")
    public BigDecimal totalRevenue(@RequestParam(name = "sinceDay") Date sinceDay,
                                   @RequestParam(name = "toDay") Date toDay){
        return statisticService.totalRevenue(sinceDay, toDay);
    }

    @GetMapping(value = "/total-success-revenue")
    public BigDecimal totalSuccessRevenue(@RequestParam(name = "sinceDay") Date sinceDay,
                                   @RequestParam(name = "toDay") Date toDay){
        return statisticService.totalSuccessRevenue(sinceDay, toDay);
    }

    @GetMapping(value = "/total-progress-revenue")
    public BigDecimal totalProgressRevenue(@RequestParam(name = "sinceDay") Date sinceDay,
                                   @RequestParam(name = "toDay") Date toDay){
        return statisticService.totalProgressRevenue(sinceDay, toDay);
    }

    @GetMapping(value = "/total-cancel-revenue")
    public BigDecimal totalCancelRevenue(@RequestParam(name = "sinceDay") Date sinceDay,
                                           @RequestParam(name = "toDay") Date toDay){
        return statisticService.totalCancelRevenue(sinceDay, toDay);
    }

    @GetMapping(value = "/order-amount")
    public int totalOrdered(){
        return statisticService.totalOrdered();
    }

    @GetMapping(value = "/top-seller")
    public List<IProductQuantity> findTopProduct(){
        return statisticService.findTopProduct();
    }

}
