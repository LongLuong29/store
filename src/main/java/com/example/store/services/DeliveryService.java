package com.example.store.services;

import com.example.store.dto.request.DeliveryRequestDTO;
import com.example.store.dto.response.DeliveryResponseDTO;
import com.example.store.dto.response.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DeliveryService {
    ResponseEntity<?> getAllDeliveryOnTrading(Pageable pageable);

    ResponseEntity<ResponseObject> createDelivery(DeliveryRequestDTO deliveryRequestDTO);

    ResponseEntity<ResponseObject> updateDelivery(DeliveryRequestDTO deliveryRequestDTO, Long id);

    ResponseEntity<ResponseObject> deleteDelivery(Long id);

    DeliveryResponseDTO getDeliveryById(Long id);

    ResponseEntity<?> getDeliveryByShipper(Long shipperId);

    List<DeliveryResponseDTO> getDeliveryByOrder(Long orderId);

    List<DeliveryResponseDTO> findDeliveriesByOrderStatus (String orderStatus);
    //    ResponseEntity<?> getDeliveryByStatusAndShipper(String status, Long shipperId)

}
