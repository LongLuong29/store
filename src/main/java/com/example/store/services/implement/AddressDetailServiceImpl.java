package com.example.store.services.implement;

import com.example.store.dto.request.AddressRequestDTO;
import com.example.store.dto.response.AddressDetailResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.Address;
import com.example.store.entities.AddressDetail;
import com.example.store.entities.User;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.AddressDetailMapper;
import com.example.store.mapper.AddressMapper;
import com.example.store.repositories.AddressDetailRepository;
import com.example.store.repositories.AddressRepository;
import com.example.store.repositories.UserRepository;
import com.example.store.services.AddressDetailService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressDetailServiceImpl implements AddressDetailService {

    @Autowired private AddressDetailRepository addressDetailRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private UserRepository userRepository;

    private final AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
    private final AddressDetailMapper addressDetailMapper = Mappers.getMapper(AddressDetailMapper.class);

    @Override
    public ResponseEntity<ResponseObject> createAddressDetail(AddressRequestDTO addressRequestDTO, Long userId) {
        Address address = addressMapper.addressRequestDTOtoAddress(addressRequestDTO);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ với userId = " + userId));
        AddressDetailResponseDTO addressDetailResponseDTO = saveUser(address, user, addressRequestDTO.isDefaultAddress());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Tạo địa chỉ mới thành công!!!", addressDetailResponseDTO));
    }

    @Override
    public ResponseEntity<?> getAddressByUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));
        List<AddressDetail> addressDetailList = addressDetailRepository.findAddressDetailsByUser(user);
        List<AddressDetailResponseDTO> addressDetailResponseDTOList = new ArrayList<>();

        for (AddressDetail addressDetail : addressDetailList){
            AddressDetailResponseDTO addressDetailResponseDTO = addressDetailMapper.addressDetailToAddressDetailResponseDTO(addressDetail);
            addressDetailResponseDTOList.add(addressDetailResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(addressDetailResponseDTOList);
    }

    @Override
    public ResponseEntity<ResponseObject> updateAddressDetail(Long addressId, Long userId, AddressRequestDTO addressRequestDTO) {
        Address address = addressMapper.addressRequestDTOtoAddress(addressRequestDTO);

        User getUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
        Address getAddress = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Could not find address with ID = " + addressId));
        AddressDetail getAddressDetail = addressDetailRepository.findAddressDetailByUserAndAddress(getUser, getAddress)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find address detail with ID user = " + userId + " and ID address = " + addressId));

        // xóa address detail cũ đi
        addressDetailRepository.delete(getAddressDetail);

        // lưu address detail mới vào hệ thống
        AddressDetailResponseDTO addressDetailResponseDTO = saveUser(address, getUser, addressRequestDTO.isDefaultAddress());

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update address detail success!!!", addressDetailResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteAddressDetail(Long addressId, Long userId) {
        User getUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));
        Address getAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find address with ID = " + addressId));
        AddressDetail getAddressDetail = addressDetailRepository.findAddressDetailByUserAndAddress(getUser, getAddress)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find address detail with ID user = " + userId + " and ID address = " + addressId));

        addressDetailRepository.delete(getAddressDetail);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete address detail successfully !!!"));
    }

    private AddressDetailResponseDTO saveUser(Address address, User user, boolean defaultAddress){
        AddressDetail addressDetail = new AddressDetail();
        addressDetail.setUser(user);

        //Check and switch default address
        List<AddressDetail> addressDetailList = addressDetailRepository.findAddressDetailsByUser(user);
        if (addressDetailList.isEmpty()){
            addressDetail.setDefaultAddress(true);
        } else {
            if (defaultAddress){
                for (AddressDetail getAddressDetail : addressDetailList){
                    if (getAddressDetail.isDefaultAddress()){
                        getAddressDetail.setDefaultAddress(false);
                        addressDetailRepository.save(getAddressDetail);
                    }
                }
            }
            addressDetail.setDefaultAddress(defaultAddress);
        }
        Optional<Address> getAddress =
                addressRepository.findAddressByProvinceAndDistrictAndWardAndApartmentNumber(address.getProvince(),
                        address.getDistrict(), address.getWard(), address.getApartmentNumber());
        //Check address is exists
        if (getAddress.isPresent()){
            address.setId(getAddress.get().getId());
            addressDetail.setAddress(address);
        } else {
            Address newAddress = addressRepository.save(address);
            addressDetail.setAddress(newAddress);
        }
        return addressDetailMapper.addressDetailToAddressDetailResponseDTO(addressDetailRepository.save(addressDetail));
    }
}
