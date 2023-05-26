package com.example.store.services.implement;

import com.example.store.dto.request.AttributeRequestDTO;
import com.example.store.dto.response.ProductAttributeResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.Attribute;
import com.example.store.entities.Product;
import com.example.store.entities.ProductAttribute;
import com.example.store.exceptions.ResourceAlreadyExistsException;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.AttributeMapper;
import com.example.store.mapper.ProductAttributeMapper;
import com.example.store.repositories.AttributeRepository;
import com.example.store.repositories.ProductAttributeRepository;
import com.example.store.repositories.ProductRepository;
import com.example.store.services.ProductAttributeService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductAttributeServiceImpl implements ProductAttributeService {

    @Autowired private AttributeRepository attributeRepository;
    @Autowired private ProductAttributeRepository productAttributeRepository;
    @Autowired private ProductRepository productRepository;

    private final AttributeMapper attributeMapper = Mappers.getMapper(AttributeMapper.class);
    private final ProductAttributeMapper productAttributeMapper = Mappers.getMapper(ProductAttributeMapper.class);


    @Override
    public ResponseEntity<ResponseObject> createProductAttribute(Long productId, AttributeRequestDTO attributeRequestDTO) {
        Attribute attribute = attributeMapper.attributeRequestDTOtoAttribute(attributeRequestDTO);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
        ProductAttributeResponseDTO productAttributeResponseDTO = saveProductAttribute(attribute, product);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Create product attribute success!!!", productAttributeResponseDTO));    }

    @Override
    public ResponseEntity<ResponseObject> updateProductAttribute(Long productId, Long attributeId, AttributeRequestDTO attributeRequestDTO) {
        Attribute attribute = attributeMapper.attributeRequestDTOtoAttribute(attributeRequestDTO);
        Product getProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));

        Attribute getAttribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find attribute with ID = " + attributeId));
        ProductAttribute productAttribute =
                productAttributeRepository.findProductAttributeByAttributeAndProduct(getAttribute, getProduct)
                        .orElseThrow(() -> new ResourceNotFoundException("Could not find address detail with ID product = " + productId + " and ID attribute = " + attributeId));
        this.productAttributeRepository.delete(productAttribute);
        ProductAttributeResponseDTO productAttributeResponseDTO = saveProductAttribute(attribute, getProduct);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Update product attribute success!!!", productAttributeResponseDTO));
    }

    @Override
    public ResponseEntity<?> getProductAttributeByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));

        List<ProductAttribute> productAttributeList = productAttributeRepository.findProductAttributeByProduct(product);
        List<ProductAttributeResponseDTO> productAttributeResponseDTOList = new ArrayList<>();

        for(ProductAttribute productAttribute : productAttributeList){
            ProductAttributeResponseDTO productAttributeResponseDTO
                    = productAttributeMapper.attributeToProductAttributeResponseDTO(productAttribute);
            productAttributeResponseDTOList.add(productAttributeResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(productAttributeResponseDTOList);
    }

    private ProductAttributeResponseDTO saveProductAttribute(Attribute attribute, Product product){
        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setProduct(product);
        //Check attribute of product is duplicate
        List<ProductAttribute> productAttributeList = productAttributeRepository.findProductAttributeByProduct(product);
        for(ProductAttribute productAttributeItem : productAttributeList){
            if(productAttributeItem.getAttribute().getName().equals(attribute.getName())){
                throw new ResourceAlreadyExistsException("Product already has this attribute");
            }
        }
        Optional<Attribute> getAttribute = attributeRepository.findAttributeByNameAndValue(attribute.getName(), attribute.getValue());
        //Check product attribute already exists
        if (getAttribute.isPresent()){
            productAttribute.setAttribute(getAttribute.get());
        } else {
            Attribute attributeSaved = attributeRepository.save(attribute);
            productAttribute.setAttribute(attributeSaved);
        }
        return productAttributeMapper.attributeToProductAttributeResponseDTO(productAttributeRepository.save(productAttribute));
    }
}
