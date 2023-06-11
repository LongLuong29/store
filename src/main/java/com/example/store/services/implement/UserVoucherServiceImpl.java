package com.example.store.services.implement;

import com.example.store.dto.response.ResponseObject;
import com.example.store.dto.response.UserVoucherResponseDTO;
import com.example.store.entities.Order;
import com.example.store.entities.User;
import com.example.store.entities.UserVoucher;
import com.example.store.entities.Voucher;
import com.example.store.exceptions.InvalidValueException;
import com.example.store.exceptions.ResourceAlreadyExistsException;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.UserVoucherMapper;
import com.example.store.repositories.OrderRepository;
import com.example.store.repositories.UserRepository;
import com.example.store.repositories.UserVoucherRepository;
import com.example.store.repositories.VoucherRepository;
import com.example.store.services.UserVoucherService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserVoucherServiceImpl implements UserVoucherService {
    @Autowired private VoucherRepository voucherRepository;
    @Autowired private UserVoucherRepository userVoucherRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;
//    private final VoucherMapper voucherMapper = Mappers.getMapper(VoucherMapper.class);
    private final UserVoucherMapper userVoucherMapper = Mappers.getMapper(UserVoucherMapper.class);


    @Override
    public ResponseEntity<?> getAllUserVoucher(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not found user with id = " + userId));
        List<UserVoucher> userVoucherList = userVoucherRepository.findUserVoucherByUser(user);
        List<UserVoucherResponseDTO> userVoucherResponseDTOList = new ArrayList<>();
        if(userVoucherList.size()==0){
            throw new ResourceNotFoundException("User does not have any voucher");
        }
        for (UserVoucher uv: userVoucherList){
            UserVoucherResponseDTO userVoucherResponseDTO = userVoucherMapper.userVoucherToUserVoucherResponseDTO(uv);
            userVoucherResponseDTOList.add(userVoucherResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userVoucherResponseDTOList);
    }
    // VALID VOUCHER
    @Override
    public ResponseEntity<?> getUserVouchersForOrder(Long userId, Long orderId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not found user with id = " + userId));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not found order with id = " + orderId));
        BigDecimal price = order.getTotalPrice(); // tong tien don hang
        List<UserVoucher> userVoucherList = userVoucherRepository.findUserVoucherByUser(user);
        List<UserVoucherResponseDTO> userVoucherResponseDTOList = new ArrayList<>();
        if(userVoucherList.size()==0){
            throw new ResourceNotFoundException("User does not have any voucher satisfied");
        }
        for (UserVoucher uv: userVoucherList){
            if(uv.getVoucher().getMinSpend() == null){
                UserVoucherResponseDTO userVoucherResponseDTO = userVoucherMapper.userVoucherToUserVoucherResponseDTO(uv);
                userVoucherResponseDTOList.add(userVoucherResponseDTO);
            }
            if(price.compareTo(uv.getVoucher().getMinSpend()) < 0){
                throw new InvalidValueException("Voucher is not meet requirement");
            }
            UserVoucherResponseDTO userVoucherResponseDTO = userVoucherMapper.userVoucherToUserVoucherResponseDTO(uv);
            userVoucherResponseDTOList.add(userVoucherResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userVoucherResponseDTOList);
    }
    //CHECK VOUCHER CODE
    @Override
    public ResponseEntity<ResponseObject> getUserVoucherByCode(Long userId, BigDecimal totalPrice, String code) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not found user with id = " + userId));
        Voucher voucher = voucherRepository.findVoucherByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Could not found voucher with code = "+code));

        BigDecimal price = totalPrice;
        UserVoucher userVoucher = userVoucherRepository.findUserVoucherByUserAndVoucher(user,voucher)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user voucher "));

        if(price.compareTo(userVoucher.getVoucher().getMinSpend()) < 0){
            throw new InvalidValueException("Total order price is not meet requirement");
        }
        UserVoucherResponseDTO userVoucherResponseDTO = userVoucherMapper.userVoucherToUserVoucherResponseDTO(userVoucher);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Get this voucher successfully", userVoucherResponseDTO));    }
    @Override
    public ResponseEntity<ResponseObject> createUserVoucher(Long userId, Long voucherId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not found user with id = " + userId));
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not found voucher with id = "+voucherId));
        //Check exits
        Optional<UserVoucher> getUserVoucher = userVoucherRepository.findUserVoucherByUserAndVoucher(user, voucher);
        if(getUserVoucher.isPresent()){
            throw new ResourceAlreadyExistsException("User already has this voucher");
        }
        UserVoucher userVoucher = new UserVoucher();
        userVoucher.setUser(user);
        userVoucher.setVoucher(voucher);
        UserVoucher userVoucherSaved = userVoucherRepository.save(userVoucher);
        UserVoucherResponseDTO userVoucherResponseDTO
                = userVoucherMapper.userVoucherToUserVoucherResponseDTO(userVoucherSaved);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Add voucher for this user successfully", userVoucherResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteUserVoucher(Long userId, Long voucherId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with id = " + userId));
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find voucher with id = "+voucherId));
        UserVoucher userVoucher = userVoucherRepository.findUserVoucherByUserAndVoucher(user,voucher)
                .orElseThrow(()-> new ResourceNotFoundException("Could not find voucher with id = "+voucherId +" in this user"));
        this.userVoucherRepository.delete(userVoucher);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK,"Remove voucher with id = "+voucherId +" in this user successfully"));
    }

    @Override
    public BigDecimal calculateVoucherDiscount(Long userId, Long voucherId, BigDecimal totalPrice, BigDecimal shippingFee) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find voucher with ID = " + voucherId));

        UserVoucher userVoucher = userVoucherRepository.findUserVoucherByUserAndVoucher(user, voucher)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find this voucher "));

        BigDecimal discountAmount;
        double rankDiscount = user.getRank().getDiscount()/100;  // rank discount
        double getUserVoucherDiscount = userVoucher.getVoucher().getPercent()/100; // get voucher discount
        double totalDiscount; // tong discount
        BigDecimal orderPrice = totalPrice;// tính tổng tiền order chưa có giảm giá
        BigDecimal shipPrice = shippingFee;// ting tong tien ship chua co giam gia
        // Người dùng không dùng voucher
        switch (voucher.getVoucherType().getName()){
            // order discount
            case "ORDER":
                // tính tổng số tiền đc giảm trên đơn hàng, chưa tính rank
                discountAmount = orderPrice.multiply(BigDecimal.valueOf(getUserVoucherDiscount));
                // tính phần trăm giảm giá của: order + rank
                totalDiscount = getUserVoucherDiscount + rankDiscount;
                // tổng tiền đc giảm > upTo số tiền gioi han.
                if(discountAmount.compareTo(voucher.getUpTo()) >0)
                {
                    discountAmount = voucher.getUpTo();
                    return discountAmount;
                }
                else // tổng số tiền đc giảm < minSpend   =>  tinh theo phan tram
                {
                    discountAmount  = orderPrice.multiply(BigDecimal.valueOf(totalDiscount));
                    return discountAmount;
                }
            //shipping discount
            case "SHIPPING":
                // tinh phi ship dc giam
                BigDecimal shipDiscount = shipPrice.multiply(BigDecimal.valueOf(getUserVoucherDiscount));
                if(shipDiscount.compareTo(voucher.getUpTo()) >0) // phi ship dc giam > minSpend => su dung minSpend
                {
                    discountAmount = orderPrice.multiply(BigDecimal.valueOf(rankDiscount)).add(voucher.getUpTo());
                    return discountAmount;
                }
                else // phi ship dc giam < min spend   =>  tinh theo phan tram
                {
                    discountAmount = orderPrice.multiply(BigDecimal.valueOf(rankDiscount)).add(shipDiscount);
                    return discountAmount;
                }
        }
        return BigDecimal.valueOf(0);
    }
}
