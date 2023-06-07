package com.example.store.services.implement;

import com.example.store.dto.request.VoucherRequestDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.dto.response.UserVoucherResponseDTO;
import com.example.store.dto.response.VoucherResponseDTO;
import com.example.store.entities.User;
import com.example.store.entities.UserVoucher;
import com.example.store.entities.Voucher;
import com.example.store.exceptions.ResourceAlreadyExistsException;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.UserVoucherMapper;
import com.example.store.mapper.VoucherMapper;
import com.example.store.repositories.UserRepository;
import com.example.store.repositories.UserVoucherRepository;
import com.example.store.repositories.VoucherRepository;
import com.example.store.services.UserVoucherService;
import com.example.store.services.VoucherService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserVoucherServiceImpl implements UserVoucherService {
    @Autowired private VoucherRepository voucherRepository;
    @Autowired private UserVoucherRepository userVoucherRepository;
    @Autowired private UserRepository userRepository;
    private final VoucherMapper voucherMapper = Mappers.getMapper(VoucherMapper.class);
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
}
