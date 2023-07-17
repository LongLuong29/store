package com.example.store.services.implement;

import com.example.store.dto.request.OrderRequestDTO;
import com.example.store.dto.response.OrderResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.*;
import com.example.store.exceptions.InvalidValueException;
import com.example.store.exceptions.ResourceAlreadyExistsException;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.OrderMapper;
import com.example.store.repositories.*;
import com.example.store.services.OrderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderProductRepository orderProductRepository;
    @Autowired private DiscountRepository discountRepository;
    @Autowired private VoucherRepository voucherRepository;
    @Autowired private UserVoucherRepository userVoucherRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private JavaMailSender mailSender;

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
        Optional<User> userCheckPhone = userRepository.findUserByPhone(user.getPhone());
        if (userCheckPhone.get().getPhone() == null) {
            throw new ResourceAlreadyExistsException("User must have phone number");
        }
        Address address = addressRepository.findById(oderRequestDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find this address"));
        Order order = orderMapper.orderRequestDTOToOrder(oderRequestDTO);

        Date orderDay = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmms");
        String orderCode = "#OC" + formatter.format(orderDay) + "U"+user.getId().toString();

        order.setUser(user);
        order.setStatus("Ordered");
        order.setOrderedDate(orderDay);
        order.setAddress(address);
        order.setOrderCode(orderCode);

        Order orderSave = orderRepository.save(order);
        OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(orderSave);

        //send mail
        try {
            sendEmailForOrderStatus(orderSave,0);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Create order success!", orderResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> upateOrder(Long orderId, OrderRequestDTO orderRequestDTO) {
        Order getOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find order with ID = " + orderId));
        Order order = orderMapper.orderRequestDTOToOrder(orderRequestDTO);
        Address address = addressRepository.findById(orderRequestDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find this address"));

        order.setId(orderId);
        order.setAddress(address);
        order.setUser(getOrder.getUser());
        order.setTotalPrice(getOrder.getTotalPrice());
        order.setCreatedDate(getOrder.getCreatedDate());

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

    @Override
    public ResponseEntity<ResponseObject> updateOrderStatus(Long orderId, String orderStatus) {
        Order getOrder = orderRepository
                .findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Could not find order with ID = " + orderId));
        Date date = new Date();
        if(getOrder.getStatus().equals("Done")){throw new InvalidValueException("Order is already finish");}
        if(orderStatus.equals("Delivered")){
            getOrder.setStatus("Delivered");
            if(getOrder.getPaymentMethod().equals("COD")){
                getOrder.setDeliveredDate(date);
                getOrder.setPaidDate(date);
            }
            else{
                getOrder.setDeliveredDate(date);
            }
            try {
                sendEmailForOrderStatus(getOrder, 2);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        if(orderStatus.equals("Done")){
//            String status = getOrder.getStatus();
//            if (status.equals("Reciev") == false)
//            {throw new InvalidValueException("Order isn't confirmed yet");}
            getOrder.setStatus("Done");
            getOrder.setDoneDate(date);
        }
        else {
            getOrder.setStatus(orderStatus);
        }

        Order orderSave = orderRepository.save(getOrder);
        OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(orderSave);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Update order status success!", orderResponseDTO));
    }

    @Override
    public ResponseEntity<?> checkoutByWallet(Long orderId, Long userId, BigDecimal totalPrice){
        Order getOrder = orderRepository
                .findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Could not find order with ID = " + orderId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
        BigDecimal userWallet = user.getWallet();
        if(totalPrice.compareTo(userWallet) > 0){throw new InvalidValueException("Ví người dùng không đủ số dư để thanh toán");}
        Date date = new Date();
        getOrder.setStatus("Wait_Delivering");
        getOrder.setPaidDate(date);
        user.setWallet(userWallet.subtract(totalPrice));
        orderRepository.save(getOrder);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("Thanh toán thành công. Ví của người dùng còn lại: "+user.getWallet()+"VND");
    }

    @Override
    public ResponseEntity<?> getListOrderByOrderStatus(String orderStatus) {
        List<Order> orderList = orderRepository.findOrdersByStatus(orderStatus);
        List<OrderResponseDTO> orderResponseDTOList = new ArrayList<>();
        for(Order o: orderList){
            OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(o);
            orderResponseDTOList.add(orderResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDTOList);
    }

    @Override
    public void sendEmailForOrderStatus(Order order, int typeMail) throws MessagingException, UnsupportedEncodingException {
        User user = order.getUser();
        List<OrderProduct> orderProductList = orderProductRepository.findOrderProductByOrder(order);
        List<String> productList = new ArrayList<>();
        Date date = new Date();

        String senderName = "GEAR STORE";
        String orderedSubject = "Notificate of order";
        String orderedMailContent = "<p>Dear " + user.getName() + ",<p><br>"
                + "You have just successfully ordered an order <b>"+order.getOrderCode()+"</b> at "+order.getOrderedDate()+"<br> <br>"
                + "Thank you for choosing us,<br>"
                + "From GEAR Store with love.";
//                + "<Please check the order information below to confirm that the products you have ordered are correct<br>"
//                + "<h4>ORDER INFORMATION: </h4><br>";
//        for(OrderProduct op: orderProductList){
//            String productName = op.getProduct().getName();
//            orderedMailContent.concat(productName + "<br>" +
//                    "<img src=\"" + op.getProduct().getThumbnail() + "\"> <br>");
//        }

        String paymentSubject = "Payment Successfully";
        String mail4PaymentNoti = "<p>Dear " + user.getName() + ",<p><br>"
                + "You have just successfully paid an order <b>"+order.getOrderCode()+"</b><br>" + "<br>"
                + "<h3> PAYMENT INFO </h3><br>"
                + "<i>Order Price: </i>" + order.getFinalPrice() + "<br>"
                + "<i>Shipping Fee: </i>" + order.getShippingFee() + "<br>"
                + "<i>Total price: </i>" + order.getTotalPrice() + "<br>" + "<br>"
                + "Thank you for choosing us,<br>"
                + "From GEAR Store with love. <br>";

        String deliveredSubject = "Order Delivered";
        String mail4DeliveredNoti = "<p>Dear " + user.getName() + ",<p><br>"
                + "Your order <b>"+order.getOrderCode()+"</b>"+ " has just been delivered at " + date + "<br>" + "<br>"
                + "Please login to GEAR Store to confirm that you have received the items and are satisfied with the product within 3 days."
                + " If you do not confirm within this time we will automatically confirm for you.<br>"
                + "<br>"
                + "Thank you for choosing us,<br>"
                + "From GEAR Store with love. <br>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        if(typeMail == 0) // 0 for notificate for ordered
        {
            messageHelper.setFrom("longluong290901@gmail.com", senderName);
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject(orderedSubject);
            messageHelper.setText(orderedMailContent, true);
            mailSender.send(message);
        }
        if(typeMail == 1) // 1 for notificate for payment successfully
        {
            messageHelper.setFrom("longluong290901@gmail.com", senderName);
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject(paymentSubject);
            messageHelper.setText(mail4PaymentNoti, true);
            mailSender.send(message);
        }
        if(typeMail == 2) // 2 for notificate for order delivered
        {
            messageHelper.setFrom("longluong290901@gmail.com", senderName);
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject(deliveredSubject);
            messageHelper.setText(mail4DeliveredNoti, true);
            mailSender.send(message);
        }
    }
}
