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
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderProductRepository orderProductRepository;
    @Autowired private DiscountRepository discountRepository;
    @Autowired private UserDiscountRepository userDiscountRepository;
//

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
    public ResponseEntity<ResponseObject> createOrder(Long userId, OrderRequestDTO oderRequestDTO, Long discountId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find discount with ID = " + discountId));

        Order order = orderMapper.orderRequestDTOToOrder(oderRequestDTO);
        order.setUser(user);

        double getUserDiscount;
        double rankDiscount = user.getRank().getDiscount();
        // người dùng không dùng discount
        if(discountId==3){
            getUserDiscount=0;
        }
        // có được phần trăm giảm giá của discount
        else{
            UserDiscount userDiscount = userDiscountRepository.findUserDiscountByDiscountAndUser(discount, user)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find discount with this user = "));
            getUserDiscount = userDiscount.getDiscount().getPercent();
        }
        double totalDiscount = (getUserDiscount + rankDiscount)/100;
        BigDecimal finalPrice = order.getTotalPrice().multiply(BigDecimal.valueOf(1-totalDiscount));

        order.setTotalPrice(finalPrice);
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
