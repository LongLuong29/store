package com.example.store.services.implement;

import com.example.store.dto.response.*;
import com.example.store.entities.*;
import com.example.store.mapper.OrderMapper;
import com.example.store.mapper.OrderProductMapper;
import com.example.store.mapper.ProductMapper;
import com.example.store.models.IProductQuantity;
import com.example.store.repositories.*;
import com.example.store.services.StatisticService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderProductRepository orderProductRepository;
    @Autowired private DeliveryRepository deliveryRepository;
    @Autowired private FirebaseImageServiceImpl imageService;

    private final OrderProductMapper orderProductMapper = Mappers.getMapper(OrderProductMapper.class);
    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);


    @Override
    public BigDecimal totalRevenue(Date sinceDay, Date toDay) {
        List<Order> orderList = orderRepository.findOrderByDate(sinceDay, toDay);
        BigDecimal totalRevenue = BigDecimal.valueOf(0);
        for (Order o : orderList) {
            totalRevenue = totalRevenue.add(o.getFinalPrice());
        }
        return totalRevenue;
    }

    @Override
    public BigDecimal totalSuccessRevenue(Date sinceDay, Date toDay) {
        List<Order> orderList = orderRepository.findOrderByDate(sinceDay, toDay);
        BigDecimal totalRevenue = BigDecimal.valueOf(0);
        for (Order o : orderList) {
            if (o.getStatus().equals("Done")) {
                totalRevenue = totalRevenue.add(o.getFinalPrice());
            }
        }
        return totalRevenue;
    }

    @Override
    public BigDecimal totalProgressRevenue(Date sinceDay, Date toDay) {
        List<Order> orderList = orderRepository.findOrderByDate(sinceDay, toDay);
        BigDecimal totalRevenue = BigDecimal.valueOf(0);
        for (Order o : orderList) {
            if (!o.getStatus().equals("Done") && !o.getStatus().equals("Cancel")) {
                totalRevenue = totalRevenue.add(o.getFinalPrice());
            }
        }
        return totalRevenue;
    }

    @Override
    public BigDecimal totalCancelRevenue(Date sinceDay, Date toDay) {
        List<Order> orderList = orderRepository.findOrderByDate(sinceDay, toDay);
        BigDecimal totalRevenue = BigDecimal.valueOf(0);
        for (Order o : orderList) {
            if (o.getStatus().equals("Cancel")) {
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
    public ResponseEntity<?> findTopProduct() {
        List<OrderProduct> topSellerProductList = orderProductRepository.findTopProduct();
        List<ProductQuantityResponseDTO> productQuantityResponseDTOList = new ArrayList<>();
        for (OrderProduct i : topSellerProductList) {
            List<OrderProduct> getProductsInOrder = orderProductRepository.findOrderProductByProduct(i.getProduct());
            int quantity = 0;
            for (OrderProduct op : getProductsInOrder) {
                quantity = quantity + op.getQuantity();
            }
            ProductResponseDTO getProduct = productMapper.productToProductResponseDTO(i.getProduct());
            ProductQuantityResponseDTO productQuantityResponseDTO = new ProductQuantityResponseDTO();
            productQuantityResponseDTO.setProduct(getProduct);
            productQuantityResponseDTO.setQuantity(quantity);
            productQuantityResponseDTOList.add(productQuantityResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(productQuantityResponseDTOList);
    }

    @Override
    public ResponseEntity<?> findSoldProductAmount(Pageable pageable) {
        Page<OrderProduct> topSoldProductList = orderProductRepository.findSoldProductAmount(pageable);
        List<ProductQuantityResponseDTO> productQuantityResponseDTOList = new ArrayList<>();
        for (OrderProduct i : topSoldProductList) {
            List<OrderProduct> getProductsInOrder = orderProductRepository.findOrderProductByProduct(i.getProduct());
            int quantity = 0;
            for (OrderProduct op : getProductsInOrder) {
                quantity = quantity + op.getQuantity();
            }
            ProductResponseDTO getProduct = productMapper.productToProductResponseDTO(i.getProduct());
            ProductQuantityResponseDTO productQuantityResponseDTO = new ProductQuantityResponseDTO();
            getProduct.setThumbnail(imageService.getImageUrl(i.getProduct().getThumbnail()));
            productQuantityResponseDTO.setProduct(getProduct);
            productQuantityResponseDTO.setQuantity(quantity);
            productQuantityResponseDTOList.add(productQuantityResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(productQuantityResponseDTOList);
    }

    @Override
    public int countOrderAmountByOrderStatus(String orderStatus) {
        if(orderStatus == null){return orderRepository.findAll().size();}
        int amount = orderRepository.findOrdersByStatus(orderStatus).size();
        return amount;
    }

    @Override
    public ResponseEntity<?> find5RecentOrder() {
        Date today = new Date();
        List<Order> orderList = orderRepository.find5RecentOrder(today);
        List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();
        for (Order o: orderList){
            OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(o);
            orderResponseDTOList.add(orderResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDTOList);
    }

    @Override
    public List<BigDecimal> totalRevenueIn7Days() {
        List<BigDecimal> totalRevenueIn7Days = new ArrayList<>();

        for(int i =0; i < 7; i++){
            Calendar sinceDay = Calendar.getInstance();
            Calendar toDay = Calendar.getInstance();
            sinceDay.add(Calendar.DATE, -i);
            toDay.add(Calendar.DATE, -i);
            sinceDay.set(Calendar.HOUR_OF_DAY, 0); sinceDay.set(Calendar.MINUTE, 0); sinceDay.set(Calendar.SECOND, 3);
            toDay.set(Calendar.HOUR_OF_DAY, 23); toDay.set(Calendar.MINUTE, 59); toDay.set(Calendar.SECOND, 58);
            List<Order> orderList = orderRepository.findOrderByDate(sinceDay.getTime(), toDay.getTime());
            BigDecimal totalRevenue = BigDecimal.valueOf(0);
            for (Order o : orderList) {
                totalRevenue = totalRevenue.add(o.getFinalPrice());
            }
            totalRevenueIn7Days.add(totalRevenue);
        }
        return totalRevenueIn7Days;
    }

    @Override
    public List<Integer> countTotalNewCustomer7Days() {
        List<Integer> newUserAmount = new ArrayList<>();

        for(int i =0; i < 7; i++){
            Calendar sinceDay = Calendar.getInstance();
            Calendar toDay = Calendar.getInstance();
            sinceDay.add(Calendar.DATE, -i);
            toDay.add(Calendar.DATE, -i);
            sinceDay.set(Calendar.HOUR_OF_DAY, 0); sinceDay.set(Calendar.MINUTE, 0); sinceDay.set(Calendar.SECOND, 3);
            toDay.set(Calendar.HOUR_OF_DAY, 23); toDay.set(Calendar.MINUTE, 59); toDay.set(Calendar.SECOND, 58);

            Integer getUserAmount = userRepository.countTotalNewCustomer7Days(sinceDay.getTime(), toDay.getTime());
            newUserAmount.add(getUserAmount);
        }
        return newUserAmount;
    }

    @Override
    public List<TopShipperResponseDTO> top5Shipper() {
        List<User> shipperList = deliveryRepository.getTopShipperByOrder();
        List<TopShipperResponseDTO> topShipperResponseDTOList = new ArrayList<>();
        for(User shipper: shipperList ){
            TopShipperResponseDTO topShipperResponseDTO = new TopShipperResponseDTO();
            List<Delivery> deliveryList = deliveryRepository.findDeliveriesByShipper(shipper);
            int orderAmount = 0;
            for(Delivery delivery: deliveryList){
                if(delivery.getOrder().getStatus().equals("Done")){
                    orderAmount = orderAmount +1;
                }
//                orderAmount = orderAmount +1;
            }
            topShipperResponseDTO.setShipperName(shipper.getName());
            topShipperResponseDTO.setOrderAmount(orderAmount);
            topShipperResponseDTOList.add(topShipperResponseDTO);
        }
        return topShipperResponseDTOList;
    }

}
