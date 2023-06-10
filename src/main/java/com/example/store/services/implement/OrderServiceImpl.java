package com.example.store.services.implement;

import com.example.store.dto.request.OrderRequestDTO;
import com.example.store.dto.response.OrderResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.*;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repositories.*;
import com.example.store.services.OrderService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderProductRepository orderProductRepository;
    @Autowired private DiscountRepository discountRepository;
    @Autowired private VoucherRepository voucherRepository;
    @Autowired private UserVoucherRepository userVoucherRepository;


    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Override
    public ResponseEntity<?> getOrderByUser(Long userId) {
        List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
        List<Order> orderList = orderRepository.findOrdersByUser(user);

        for(Order order: orderList){
            OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(order);
            orderResponseDTOList.add(orderResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDTOList);
    }

    @Override
    public ResponseEntity<ResponseObject> createOrder(Long userId, OrderRequestDTO oderRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
//        Voucher voucher = voucherRepository.findById(voucherId)
//                .orElseThrow(() -> new ResourceNotFoundException("Could not find voucher with ID = " + voucherId));
        Order order = orderMapper.orderRequestDTOToOrder(oderRequestDTO);
        order.setUser(user);
        order.setStatus("Ordered");
        Date orderDay = new java.util.Date();
        order.setOrderedDate(orderDay);
//        UserVoucher userVoucher = userVoucherRepository.findUserVoucherByUserAndVoucher(user, voucher)
//                .orElseThrow(() -> new ResourceNotFoundException("Could not find this voucher "));
//        BigDecimal finalPrice;
//        BigDecimal discountAmount;
//        double rankDiscount = user.getRank().getDiscount()/100;  // rank discount
//        double getUserVoucherDiscount = userVoucher.getVoucher().getPercent()/100; // get voucher discount
//        double totalDiscount; // tong discount
//        BigDecimal orderPrice = order.getTotalPrice();// tính tổng tiền order chưa có giảm giá
//        BigDecimal shipPrice = order.getShippingFee();// ting tong tien ship chua co giam gia
//        // Người dùng không dùng voucher
//        switch (voucher.getVoucherType().getName()){
//            case "ORDER":
//                // order discount
//                discountAmount = orderPrice.multiply(BigDecimal.valueOf(getUserVoucherDiscount)); // tính tổng số tiền đc giảm trên đơn hàng, chưa tính rank
//                totalDiscount = getUserVoucherDiscount + rankDiscount; // tính phần trăm giảm giá của: order + rank
//                if(discountAmount.compareTo(voucher.getUpTo()) >0) // tổng tiền đc giảm > upTo số tiền yêu cầu
//                {
//                    finalPrice = orderPrice.multiply(BigDecimal.valueOf(1-rankDiscount)).add(shipPrice)
//                            .subtract(voucher.getUpTo());
//                }
//                else // tổng số tiền đc giảm < minSpend   =>  tinh theo phan tram
//                {
//                    finalPrice = orderPrice.multiply(BigDecimal.valueOf(1-totalDiscount)).add(shipPrice);
//                }
//                break;
//            case "SHIPPING":
//                //shipping discount
//                BigDecimal shipDiscount = order.getShippingFee().multiply(BigDecimal.valueOf(getUserVoucherDiscount));// tinh phi ship dc giam
//                if(shipDiscount.compareTo(voucher.getUpTo()) >0) // phi ship dc giam > minSpend => su dung minSpend
//                {
//                    finalPrice = orderPrice.multiply(BigDecimal.valueOf(rankDiscount)).add(shipPrice).subtract(shipDiscount);
//                }
//                else // phi ship dc giam < min spend   =>  tinh theo phan tram
//                {
//                    finalPrice = (orderPrice.multiply(BigDecimal.valueOf(1-rankDiscount)))  .add   (shipPrice.subtract(shipDiscount));
//                }
//                break;
//            default:
//                finalPrice = orderPrice.multiply(BigDecimal.valueOf(1-rankDiscount));
//        }
//        order.setFinalPrice(finalPrice);
        Order orderSave = orderRepository.save(order);
        OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(orderSave);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Create order success!", orderResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> upateOrder(Long orderId, OrderRequestDTO orderRequestDTO) {
        Order getOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find order with ID = " + orderId));
        Order order = orderMapper.orderRequestDTOToOrder(orderRequestDTO);

        order.setId(orderId);
        order.setTotalPrice(getOrder.getTotalPrice());
        order.setUser(getOrder.getUser());
        Order orderSave = orderRepository.save(order);
        OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(orderSave);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Update order success!", orderResponseDTO));
    }

    @Override
    public ResponseEntity<?> getAllOrder() {
        List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();
        List<Order> orderList = orderRepository.findAll();
        for (Order order : orderList){
            OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(order);
            orderResponseDTOList.add(orderResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDTOList);
    }

    @Override
    public ResponseEntity<ResponseObject> deleteOrder(Long orderId) {
        Order getOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find order with ID = " + orderId));
        List<OrderProduct> orderProductList = orderProductRepository.findOrderProductByOrder(getOrder);
        orderProductRepository.deleteAll(orderProductList);
        orderRepository.delete(getOrder);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Delete order success!"));
    }

    @Override
    public ResponseEntity<?> getOrderById(Long id) {
        Order getOrder = orderRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find order with ID = " + id));
        OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(getOrder);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDTO);
    }
}
