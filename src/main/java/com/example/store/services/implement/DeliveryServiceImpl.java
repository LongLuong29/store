package com.example.store.services.implement;

import com.example.store.dto.request.DeliveryRequestDTO;
import com.example.store.dto.response.DeliveryResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.Address;
import com.example.store.entities.Order;
import com.example.store.entities.Delivery;
import com.example.store.entities.User;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.DeliveryMapper;
import com.example.store.models.ItemTotalPage;
import com.example.store.repositories.*;
import com.example.store.services.DeliveryService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService {
    @Autowired private DeliveryRepository deliveryRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private AddressDetailRepository addressDetailRepository;

    private final DeliveryMapper mapper = Mappers.getMapper(DeliveryMapper.class);

    @Override
    public ResponseEntity<?> getAllDeliveryOnTrading(Pageable pageable) {
        Page<Delivery> getDeliveryList = deliveryRepository.findAll(pageable);
        List<Delivery> deliveryList = getDeliveryList.getContent();
        List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();

        for (Delivery d : deliveryList) {
            DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(d);
            deliveryResponseDTOList.add(deliveryResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ItemTotalPage(deliveryResponseDTOList, getDeliveryList.getTotalPages()));
    }

    @Override
    public ResponseEntity<ResponseObject> createDelivery(DeliveryRequestDTO deliveryRequestDTO) {
        Delivery delivery = mapper.deliveryRequestDTOToDelivery(deliveryRequestDTO);

        User user = userRepository.findById(deliveryRequestDTO.getShipperId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy User với ID = " + deliveryRequestDTO.getShipperId()));

        Order order = orderRepository.findById(deliveryRequestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Order với ID = " + deliveryRequestDTO.getOrderId()));
        Address address = addressRepository.findById(deliveryRequestDTO.getAddressId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Address với ID = " + deliveryRequestDTO.getAddressId()));

        delivery.setShipper(user);
        delivery.setAddress(address);
        delivery.setOrder(order);

        Delivery deliverySaved = deliveryRepository.save(delivery);
        DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(deliverySaved);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Create delivery successfully!", deliveryResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> updateDelivery(DeliveryRequestDTO deliveryRequestDTO, Long id) {
        Delivery delivery = mapper.deliveryRequestDTOToDelivery(deliveryRequestDTO);
        Delivery getDelivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find delivery with ID = " + id));
        delivery.setId(id);

        Order order = orderRepository.findById(deliveryRequestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find order with ID = " + deliveryRequestDTO.getOrderId()));
        delivery.setOrder(order);

        delivery.setAddress(getDelivery.getAddress());
        delivery.setShipper(getDelivery.getShipper());

        // change shipper
        if (deliveryRequestDTO.getShipperId() != null) {
            Optional<User> newShipper = userRepository.findById(deliveryRequestDTO.getShipperId());
            if (newShipper.isPresent()) {
                delivery.setShipper(newShipper.get());
            }
        }

        //Update order payed
        if (deliveryRequestDTO.getOrderStatus() != null){
            delivery.getOrder().setStatus(deliveryRequestDTO.getOrderStatus());
            delivery.getOrder().setPaidDate(deliveryRequestDTO.getPayDate());
            orderRepository.save(delivery.getOrder());
        }

        Delivery deliverySaved = deliveryRepository.save(delivery);
        DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(deliverySaved);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Update delivery successfully!", deliveryResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find delivery with ID = " + id));
        deliveryRepository.delete(delivery);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete delivery successfully!"));
    }

    @Override
    public DeliveryResponseDTO getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find delivery with ID = " + id));
        DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(delivery);

        return deliveryResponseDTO;
    }

    @Override
    public ResponseEntity<?> getDeliveryByStatus(String status) {
        List<Delivery> deliveryList = deliveryRepository.findDeliveriesByStatus(status);
        List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();

        for (Delivery d : deliveryList) {
            DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(d);
            deliveryResponseDTOList.add(deliveryResponseDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(deliveryResponseDTOList);
    }



    @Override
    public ResponseEntity<?> getDeliveryByShipper(Long shipperId) {
        User shipper = userRepository
                .findById(shipperId).orElseThrow(() -> new ResourceNotFoundException("Could not find shipper with ID = " + shipperId));
        List<Delivery> deliveryList = deliveryRepository.findDeliveriesByShipper(shipper);
        List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();

        for (Delivery d : deliveryList) {
            DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(d);
            deliveryResponseDTOList.add(deliveryResponseDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(deliveryResponseDTOList);
    }

    @Override
    public ResponseEntity<?> getDeliveryByStatusAndShipper(String status, Long shipperId) {
        User shipper = userRepository.findById(shipperId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find shipper with ID = " + shipperId));
        List<Delivery> deliveryList = deliveryRepository.findDeliveriesByStatusAndShipper(status, shipper);
        List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();

        for (Delivery d : deliveryList) {
            DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(d);
            deliveryResponseDTOList.add(deliveryResponseDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(deliveryResponseDTOList);
    }

    @Override
    public DeliveryResponseDTO getDeliveryByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find shipper with ID = " + orderId));

        Delivery delivery = deliveryRepository.findDeliveryByOrder(order);
        DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(delivery);
        return deliveryResponseDTO;
    }

}
