package com.example.store.services.implement;

import com.example.store.dto.response.OrderProductResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.Order;
import com.example.store.entities.OrderProduct;
import com.example.store.entities.Product;
import com.example.store.entities.ProductDiscount;
import com.example.store.exceptions.InvalidValueException;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.OrderProductMapper;
import com.example.store.mapper.UserMapper;
import com.example.store.repositories.*;
import com.example.store.services.OrderProductService;
import com.example.store.utils.Utils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderProductServiceImpl implements OrderProductService {
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderProductRepository orderProductRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private ProductDiscountRepository productDiscountRepository;
    @Autowired private DiscountRepository discountRepository;
    @Autowired private FirebaseImageServiceImpl imageService;


    private final OrderProductMapper orderProductMapper = Mappers.getMapper(OrderProductMapper.class);
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    @Override
    public ResponseEntity<ResponseObject> addProductToOrder(Long orderId, Long productId, int amount) {
        OrderProduct orderProduct = new OrderProduct();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find order with ID = " + orderId));
//        BigDecimal totalPrice = order.getTotalPrice();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
        List<ProductDiscount> productDiscountList = productDiscountRepository.findProductDiscountByProduct(product);
        Optional<OrderProduct> getOrderProduct = orderProductRepository.findOrderProductByOrderAndProduct(order, product);

        orderProduct.setPricePerOne(product.getPrice());
        if (amount > product.getInventory()){
            throw new InvalidValueException("Amount product " + product.getName() + " must be less than amount product exists");
        } else if(amount < 0) {
            throw new InvalidValueException("Amount product must greater than 1");
        } else {
            //Update amount product
            int newAmountProduct = product.getInventory() - amount;
            product.setInventory(newAmountProduct);
            productRepository.save(product);
            if (getOrderProduct.isPresent()){
                int newAmount = getOrderProduct.get().getQuantity() + amount;
                orderProduct = getOrderProduct.get();
                orderProduct.setQuantity(newAmount);
            } else {
                orderProduct.setOrder(order);
                orderProduct.setProduct(product);
                orderProduct.setQuantity(amount);
            }
            BigDecimal discountPrice = product.getPrice();
            if(productDiscountList.size() != 0){
                discountPrice = product.getPrice()
                        .multiply(BigDecimal.valueOf( 1 - productDiscountList.get(0).getDiscount().getPercent()/100));
            }
            orderProduct.setDiscountPrice(discountPrice);
//            //Total price of order
//            totalPrice = totalPrice.add(totalPrice(product, amount, discount));
//            order.setTotalPrice(totalPrice);
//            orderRepository.save(order);
        }
        OrderProductResponseDTO  orderProductResponseDTO
                =  orderProductMapper.orderProductToOrderProductResponseDTO(orderProductRepository.save(orderProduct));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Add product to order success!", orderProductResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteProductToOrder(Long orderId, Long productId) {

        return null;
    }

    @Override
    public ResponseEntity<?> getProductByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find order with ID = " + orderId));
        List<OrderProduct> orderProductList = orderProductRepository.findOrderProductByOrder(order);
        List<OrderProductResponseDTO> orderProductResponseDTOList = new ArrayList<>();

        for (OrderProduct b : orderProductList) {
            OrderProductResponseDTO orderProductResponseDTO = orderProductMapper.orderProductToOrderProductResponseDTO(b);
            orderProductResponseDTO.setProductImage(imageService.getImageUrl(b.getProduct().getThumbnail()));
            orderProductResponseDTOList.add(orderProductResponseDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(orderProductResponseDTOList);
    }

    @Override
    public ResponseEntity<?> getAllProductByOrderPayed() {

        // sap xep giam dan theo truong "createdDate"
        Sort sort = Sort.by("createdDate").descending();
        List<Order> orderList = orderRepository.findAll(sort);
        List<OrderProductResponseDTO> orderProductResponseDTOList = new ArrayList<>();

        for (Order order : orderList) {
            List<OrderProduct> orderProductList = orderProductRepository.findOrderProductByOrder(order);
            for (OrderProduct orderProduct : orderProductList) {
                OrderProductResponseDTO orderProductResponseDTO = orderProductMapper.orderProductToOrderProductResponseDTO(orderProduct);
                List<ProductDiscount> productDiscountList = productDiscountRepository.findProductDiscountByProduct(orderProduct.getProduct());

                orderProductResponseDTO.setPrice
                        (Utils.getTotalPrice(orderProduct.getProduct(), BigDecimal.valueOf(0.00), orderProduct.getQuantity(), productDiscountList.get(0)));
                orderProductResponseDTO.setUserResponseDTO(userMapper.userToUserResponseDTO(order.getUser()));
                orderProductResponseDTO.setProductImage(imageService.getImageUrl(orderProduct.getProduct().getThumbnail()));
                orderProductResponseDTOList.add(orderProductResponseDTO);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(orderProductResponseDTOList);
    }

    private BigDecimal totalPrice(Product product, int amount, double discount){
        BigDecimal totalPrice;
        totalPrice = product.getPrice().multiply(BigDecimal.valueOf( (1 - discount/100) * amount))
                .setScale(2, RoundingMode.UP);
        return totalPrice;
    }
}
