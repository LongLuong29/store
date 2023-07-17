package com.example.store.services.implement;

import com.example.store.dto.request.VoucherRequestDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.dto.response.VoucherResponseDTO;
import com.example.store.entities.Voucher;
import com.example.store.exceptions.ResourceAlreadyExistsException;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.VoucherMapper;
import com.example.store.repositories.VoucherRepository;
import com.example.store.services.VoucherService;
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
public class VoucherServiceImpl implements VoucherService {
    @Autowired private VoucherRepository voucherRepository;
    private final VoucherMapper mapper = Mappers.getMapper(VoucherMapper.class);

    @Override
    public ResponseEntity<?> getAllVoucher() {
        List<Voucher> voucherList = voucherRepository.findAll();
        List<VoucherResponseDTO> voucherResponseDTOList = new ArrayList<>();
        for(Voucher v: voucherList){
            VoucherResponseDTO voucherResponseDTO = mapper.voucherToVoucherResponseDTO(v);
            voucherResponseDTOList.add(voucherResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(voucherResponseDTOList);
    }

    @Override
    public ResponseEntity<?> decreaseVoucherQuantity(Long voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not found voucher with id = "+voucherId));
        int voucherQuantity = voucher.getQuantity();
        if(voucherQuantity > 0) {
            voucher.setQuantity(voucherQuantity - 1);
            voucherRepository.save(voucher);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Voucher quantity: " + voucher.getQuantity());
    }

    @Override
    public ResponseEntity<ResponseObject> createVoucher(VoucherRequestDTO voucherRequestDTO) {
        Voucher voucher = mapper.voucherRequestDTOtoVoucher(voucherRequestDTO);
        voucher.setStatus(true);
        checkVoucherCodeExist(voucher);
        if(voucher.getMinSpend() ==null){
            voucher.setMinSpend(BigDecimal.valueOf(0));
        }
        Voucher voucherSaved = voucherRepository.save(voucher);
        VoucherResponseDTO voucherResponseDTO = mapper.voucherToVoucherResponseDTO(voucherSaved);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK,"Create voucher successfully",voucherResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> updateVoucher(VoucherRequestDTO voucherRequestDTO, Long id) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> deleteVoucher(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not found voucher with id = "+id));
        voucherRepository.delete(voucher);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Remove voucher completely !!!"));
    }

    @Override
    public VoucherResponseDTO getVoucherById(Long id) {
        return null;
    }

    private Voucher checkVoucherCodeExist(Voucher voucher){
        Optional<Voucher> getVoucher = voucherRepository.findVoucherByCode(voucher.getCode());
        if(getVoucher.isPresent()){
            if(voucher.getId() == null){
                throw new ResourceAlreadyExistsException("Voucher code already exists");
            } else {
                if (voucher.getId() != getVoucher.get().getId()) {
                    throw new ResourceAlreadyExistsException("Brand name already exists");
                }
            }
        }
        return voucher;
    }
}
