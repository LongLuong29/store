package com.example.store.services.implement;

import com.example.store.dto.request.DeliveryRequestDTO;
import com.example.store.dto.response.DeliveryResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.Address;
import com.example.store.entities.Order;
import com.example.store.entities.Delivery;
import com.example.store.entities.User;
import com.example.store.exceptions.InvalidValueException;
import com.example.store.exceptions.ResourceAlreadyExistsException;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired private FirebaseImageServiceImpl imageService;

    private final DeliveryMapper mapper = Mappers.getMapper(DeliveryMapper.class);

    @Override
    public ResponseEntity<?> getAllDeliveryOnTrading(Pageable pageable) {
        Page<Delivery> getDeliveryList = deliveryRepository.findAllEnableDelivery(pageable);
        List<Delivery> deliveryList = getDeliveryList.getContent();
        List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();

        for (Delivery d : deliveryList) {
            if(d.isStatus()){
                DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(d);
                deliveryResponseDTOList.add(deliveryResponseDTO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ItemTotalPage(deliveryResponseDTOList, getDeliveryList.getTotalPages()));
    }
    @Override
    public ResponseEntity<ResponseObject> createDelivery(DeliveryRequestDTO deliveryRequestDTO) {
        Delivery delivery = mapper.deliveryRequestDTOToDelivery(deliveryRequestDTO);
        User user = userRepository.findById(deliveryRequestDTO.getShipperId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy User với ID = " + deliveryRequestDTO.getShipperId()));
        Order order = orderRepository.findById(deliveryRequestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Order với ID = " + deliveryRequestDTO.getOrderId()));
        Address address = addressRepository.findById(deliveryRequestDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Address với ID = " + deliveryRequestDTO.getAddressId()));
        // check error
        if(user.getRole().getId() != 3){throw new InvalidValueException("This user is not shipper");}
        if(user.getPoint() < 70){throw new InvalidValueException("Shipper không đủ số điểm tin cậy");}
        List<Delivery> userDeliveryList = deliveryRepository.findDeliveryByOrder(order);
        for (Delivery userDelivery: userDeliveryList){
            if(userDelivery != null && userDelivery.isStatus()){
                throw new ResourceAlreadyExistsException("This order already has delivery");
            }
        }
        delivery.setStatus(true);
        delivery.setShipper(user);
        delivery.setAddress(address);
        delivery.setOrder(order);
        delivery.getOrder().setStatus("Delivering");

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

        delivery.setCreatedDate(getDelivery.getCreatedDate());
        delivery.setAddress(getDelivery.getAddress());
        delivery.setShipper(getDelivery.getShipper());
        delivery.setStatus(true);
        // change shipper
        if (deliveryRequestDTO.getShipperId() != null) {
            Optional<User> newShipper = userRepository.findById(deliveryRequestDTO.getShipperId());
            if (newShipper.isPresent()) {
                delivery.setShipper(newShipper.get());
            }
        }
        if(deliveryRequestDTO.getImage() != null){
            try {
                delivery.setImage(imageService.save(deliveryRequestDTO.getImage()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(getDelivery.getImage()!=null){
                try {
                    imageService.delete(getDelivery.getImage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
//        //Update order payed
//        if (deliveryRequestDTO.getOrderStatus() != null){
//            delivery.getOrder().setStatus(deliveryRequestDTO.getOrderStatus());
//            delivery.getOrder().setPaidDate(deliveryRequestDTO.getPayDate());
//            orderRepository.save(delivery.getOrder());
//        }

        Delivery deliverySaved = deliveryRepository.save(delivery);
        DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(deliverySaved);
        deliveryResponseDTO.setImage(imageService.getImageUrl(delivery.getImage()));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Update delivery successfully!", deliveryResponseDTO));
    }
    @Override
    public ResponseEntity<ResponseObject> softDeleteDelivery(Long id, boolean deleted) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find delivery with ID = " + id));
        delivery.setStatus(deleted);
        if(delivery.getImage()!=null){
            try {
                imageService.delete(delivery.getImage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete delivery successfully!"));
    }
    @Override
    public DeliveryResponseDTO getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find delivery with ID = " + id));
        DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(delivery);
        if(delivery.getImage()!=null){deliveryResponseDTO.setImage(imageService.getImageUrl(delivery.getImage()));}
        return deliveryResponseDTO;
    }

    @Override
    public ResponseEntity<?> getDeliveryByShipper(Long shipperId) {
        User shipper = userRepository
                .findById(shipperId).orElseThrow(() -> new ResourceNotFoundException("Could not find delivery with shipper ID = " + shipperId));
        List<Delivery> deliveryList = deliveryRepository.findDeliveriesByShipper(shipper);
        List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();

        for (Delivery d : deliveryList) {
            DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(d);
            if(d.getImage()!=null){deliveryResponseDTO.setImage(imageService.getImageUrl(d.getImage()));}
            deliveryResponseDTOList.add(deliveryResponseDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(deliveryResponseDTOList);
    }

    @Override
    public List<DeliveryResponseDTO> getDeliveryByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find delivery with order ID = " + orderId));

        List<Delivery> deliveryList = deliveryRepository.findDeliveryByOrder(order);
        List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();
        for (Delivery d: deliveryList){
            DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(d);
            if(d.getImage()!=null){deliveryResponseDTO.setImage(imageService.getImageUrl(d.getImage()));}
            deliveryResponseDTOList.add(deliveryResponseDTO);
        }
        return deliveryResponseDTOList;
    }

    @Override
    public List<DeliveryResponseDTO> findDeliveriesByOrderStatus (String orderStatus) {
        List<Delivery> deliveryList = deliveryRepository.findDeliveriesByOrderStatus(orderStatus);
        List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();

        for (Delivery d : deliveryList) {
            DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(d);
            if(d.getImage()!=null){deliveryResponseDTO.setImage(imageService.getImageUrl(d.getImage()));}
            deliveryResponseDTOList.add(deliveryResponseDTO);
        }

        return deliveryResponseDTOList;
    }

    @Override
    public ResponseEntity<?> findDelivering(){
        List<Delivery> deliveryList = deliveryRepository.findDeliveriesByOrderStatus("Delivering");
        List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();
        Date now = new Date();
        for(Delivery d: deliveryList){
            if(d.getCreatedDate().compareTo(now) == 0){
                DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(d);
                deliveryResponseDTOList.add(deliveryResponseDTO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(deliveryResponseDTOList);
    }

    @Override
    public ResponseEntity<?> lateDelivering(){
        List<Delivery> deliveryList = deliveryRepository.findDeliveriesByOrderStatus("Delivering");
        List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();
        // Date Format In Java
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date now = new Date();
        String nowDate = simpleDateFormat.format(now);
        for(Delivery d: deliveryList){
            if(d.isStatus()){
                String createdDate = simpleDateFormat.format(d.getCreatedDate());
                if(!createdDate.equals(nowDate)){
                    DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(d);
                    deliveryResponseDTOList.add(deliveryResponseDTO);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(deliveryResponseDTOList);
    }

    @Override
    public ResponseEntity<?> cancelDelivery(Long deliveryId){
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find delivery with ID = " + deliveryId));
        Order order = orderRepository.findById(delivery.getOrder().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Could not find order with ID = " + delivery.getOrder().getId()));
        if(!delivery.isStatus()){throw new ResourceAlreadyExistsException("Đơn vận chuyển của shipper này đi bị hủy trước đó !!");}
        User shipper = delivery.getShipper();
        if(shipper.getPoint() < 100){delivery.getShipper().setPoint(shipper.getPoint()-10);}
        this.userRepository.save(shipper);
        delivery.setStatus(false);
        this.deliveryRepository.save(delivery);
        order.setStatus("Wait_Delivering");
        this.orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.OK).body("Hủy thành công đơn vận chuyển của shipper: " + delivery.getShipper().getName());
    }


    //    @Override
//    public ResponseEntity<?> getDeliveryByStatusAndShipper(String status, Long shipperId) {
//        User shipper = userRepository.findById(shipperId)
//                .orElseThrow(() -> new ResourceNotFoundException("Could not find shipper with ID = " + shipperId));
//        List<Delivery> deliveryList = deliveryRepository.findDeliveriesByStatusAndShipper(status, shipper);
//        List<DeliveryResponseDTO> deliveryResponseDTOList = new ArrayList<>();
//
//        for (Delivery d : deliveryList) {
//            DeliveryResponseDTO deliveryResponseDTO = mapper.deliveryToDeliveryResponseDTO(d);
//            deliveryResponseDTOList.add(deliveryResponseDTO);
//        }
//
//        return ResponseEntity.status(HttpStatus.OK).body(deliveryResponseDTOList);
//    }
}
