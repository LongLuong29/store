package com.example.store.controller;

import com.example.store.services.StatisticService;
import com.example.store.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<?> findTopProduct(){
        return statisticService.findTopProduct();
    }
    @GetMapping(value = "/sold-product-amount")
    public ResponseEntity<?> findSolProductAmount(@RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
                                                  @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        return statisticService.findSoldProductAmount(pageable);
    }

    @GetMapping(value = "/order-amount-by-status")
    public int findOrderAmountByStatus(@RequestParam(name = "orderStatus", required = false) String orderStatus){
        return statisticService.countOrderAmountByOrderStatus(orderStatus);
    }

    @GetMapping(value = "/total-revenue-in-7days")
    public List<BigDecimal> totalRevenueIn7Days(){
        return statisticService.totalRevenueIn7Days();
    }

}
