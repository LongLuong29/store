package com.example.store.services.implement;

import com.example.store.dto.request.GroupProductRequestDTO;
import com.example.store.dto.response.GroupProductResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.GroupProduct;
import com.example.store.exceptions.ResourceAlreadyExistsException;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.GroupProductMapper;
import com.example.store.repositories.GroupProductRepository;
import com.example.store.services.GroupProductService;
import jakarta.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupProductServiceImpl implements GroupProductService {

    private final GroupProductMapper mapper = Mappers.getMapper(GroupProductMapper.class);

    @Autowired private GroupProductRepository groupProductRepository;

    @Override
    public ResponseEntity<?> getAllGroupProduct() {
        List<GroupProduct> getGroupProductList = groupProductRepository.findAll();
        List<GroupProductResponseDTO> groupProductResponseDTOList = new ArrayList<>();
        for(GroupProduct b: getGroupProductList){
            GroupProductResponseDTO groupProductResponseDTO = mapper.groupProductToGroupProductResponseDTO(b);
            groupProductResponseDTOList.add(groupProductResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(groupProductResponseDTOList);
    }

    @Override
    public ResponseEntity<ResponseObject> createGroupProduct(GroupProductRequestDTO groupProductRequestDTO) {
        GroupProduct groupProduct = mapper.groupProductRequestDTOToGroupProduct(groupProductRequestDTO);
        groupProduct.setStatus(true);
        groupProduct = checkExits(groupProduct);
        GroupProduct groupProductSaved = groupProductRepository.save(groupProduct);
        GroupProductResponseDTO groupProductResponseDTO = mapper.groupProductToGroupProductResponseDTO(groupProductSaved);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Create GroupProduct successfully!", groupProductResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> updateGroupProduct(GroupProductRequestDTO groupProductRequestDTO, Long id) {
        GroupProduct groupProduct = mapper.groupProductRequestDTOToGroupProduct(groupProductRequestDTO);
        GroupProduct getGroupProduct = groupProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find GroupProduct with ID = " + id));
        groupProduct.setId(id);
        groupProduct.setStatus(true);
        groupProduct.setCreatedDate(getGroupProduct.getCreatedDate());
        groupProduct = checkExits(groupProduct);

        GroupProduct groupProductSaved = groupProductRepository.save(groupProduct);
        GroupProductResponseDTO groupProductResponseDTO = mapper.groupProductToGroupProductResponseDTO(groupProductSaved);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Update GroupProduct successfully!", groupProductResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteGroupProduct(Long id) {
        GroupProduct getGroupProduct = groupProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find GroupProduct with ID = " + id));
        groupProductRepository.delete(getGroupProduct);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK,"Delete GroupProduct successfully!"));
    }

    @Override
    public ResponseEntity<ResponseObject> softDeleteGroupProduct(Long id, boolean deleted) {
        GroupProduct getGroupProduct = groupProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find GroupProduct with ID = " + id));
        getGroupProduct.setStatus(deleted);
        groupProductRepository.save(getGroupProduct);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK,"Delete GroupProduct successfully!", deleted));
    }

    @Override
    public GroupProductResponseDTO getGroupProductById(Long id) {
        GroupProduct getGroupProduct = groupProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find GroupProduct with ID = " + id));
        GroupProductResponseDTO groupProductResponseDTO = mapper.groupProductToGroupProductResponseDTO(getGroupProduct);
        return groupProductResponseDTO;
    }

    //check GroupProduct name exits or not
    private GroupProduct checkExits(GroupProduct groupProduct){
        Optional<GroupProduct> getGroupProduct = groupProductRepository.findGroupProductByName(groupProduct.getName());
        if(getGroupProduct.isPresent()){
            if(groupProduct.getId() == null){
                throw new ResourceAlreadyExistsException("GroupProduct name already exists");
            } else {
                if (groupProduct.getId() != getGroupProduct.get().getId()) {
                    throw new ResourceAlreadyExistsException("GroupProduct name already exists");
                }
            }
        }
        return groupProduct;
    }
}
