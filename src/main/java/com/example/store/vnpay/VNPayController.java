package com.example.store.vnpay;

import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.Order;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.repositories.OrderRepository;
import com.example.store.services.OrderService;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class VNPayController {

    @Autowired private VNPayService vnPayService;
    @Autowired private OrderService orderService;
    @Autowired private OrderRepository orderRepository;

    @GetMapping("")
    public String home(){
        return "index";
    }

    @GetMapping("/create-payment")
    public ResponseEntity<ResponseObject> submidOrder(@RequestParam("amount") int orderTotal,
                                                      @RequestParam("orderId") Long orderId
                                                      /*HttpServletRequest request*/){
        /*String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();*/
        String returnUrl = "http://localhost:8080/vnpay-payment";
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderId, returnUrl);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Successfully", vnpayUrl));
    }


    @GetMapping("/vnpay-payment")
    public void GetMapping(HttpServletRequest request, HttpServletResponse response, Model model)throws IOException, ServletException {
        int paymentStatus =vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String TxnRef = request.getParameter("vnp_TxnRef");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        int stringLength = TxnRef.length() - 7;
        String orderId = String.valueOf(TxnRef.charAt(0));
        for(int i=1; i < stringLength; i++){
            String od = String.valueOf(TxnRef.charAt(i));
            orderId = orderId+od;
        }
        Optional<Order> order = orderRepository.findById(Long.parseLong(orderId));
        if(order.get() == null){throw new ResourceNotFoundException("This order isn't exists");}

        String successUrl = "http://localhost:3000/successOrder";
        String failureUrl = "http://localhost:3000/failureOrder";
        //Thanh toan thanh cong
        if(paymentStatus == 1){
            try {
                Date date = new Date();
                order.get().setPaidDate(date);
                orderRepository.save(order.get());
                orderService.sendEmailForOrderStatus(order.get(),1);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            response.sendRedirect(successUrl);
        }
        // Thanh toan that bai
        else{response.sendRedirect(failureUrl); }
    }
}
