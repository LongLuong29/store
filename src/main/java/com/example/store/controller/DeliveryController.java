package com.example.store.controller;


import com.example.store.dto.request.DeliveryRequestDTO;
import com.example.store.dto.response.DeliveryResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.services.DeliveryService;
import com.example.store.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/delivery")
public class DeliveryController {
    @Autowired private DeliveryService deliveryService;

    @GetMapping(value = "")
    public ResponseEntity<?> getAllDeliveryOnTrading(
            @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        return deliveryService.getAllDeliveryOnTrading(pageable);
    }
    @PostMapping(value = "")
    public ResponseEntity<ResponseObject> createDelivery(@ModelAttribute @Valid DeliveryRequestDTO deliveryRequestDTO){
        return deliveryService.createDelivery(deliveryRequestDTO);
    }
    @PutMapping(value = "")
    public ResponseEntity<ResponseObject> updateDelivery(@RequestParam(name = "deliveryId") Long deliveryId,
                                                         @ModelAttribute @Valid DeliveryRequestDTO deliveryRequestDTO){
        return deliveryService.updateDelivery(deliveryRequestDTO,deliveryId);
    }
    @DeleteMapping(value = "/remove")
    public ResponseEntity<ResponseObject> deleteDelivery(@RequestParam(name = "deliveryId") Long deliveryId){
        return deliveryService.deleteDelivery(deliveryId);
    }
    @GetMapping(value = "/byId")
    public DeliveryResponseDTO getDeliveryById(@RequestParam(name = "deliveryId") Long deliveryId){
        return deliveryService.getDeliveryById(deliveryId);
    }
    @GetMapping(value = "/shipper")
    public ResponseEntity<?> getDeliveryByShipper(@RequestParam(name = "shipperId") Long shipperId){
        return deliveryService.getDeliveryByShipper(shipperId);
    }
    @GetMapping(value = "/order")
    public DeliveryResponseDTO getDeliveryByOrder(@RequestParam(name = "orderId") Long orderId){
        return deliveryService.getDeliveryByOrder(orderId);
    }
//    @GetMapping(value = "/status")
//    public ResponseEntity<?> getDeliveryByStatus(@Valid String status) {
//        return deliveryService.getDeliveryByStatus(status);
//    }
//    @GetMapping(value = "/shipper/status")
//    public ResponseEntity<?> getDeliveryByStatusAndShipper(@Valid String status,
//                                                           @RequestParam(name = "shipperId") Long shipperId){
//        return deliveryService.getDeliveryByStatusAndShipper(status,shipperId);
//    }
}
