package com.example.store.services.implement;

import com.example.store.dto.response.ProductDiscountResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.Discount;
import com.example.store.entities.Product;
import com.example.store.entities.ProductDiscount;
import com.example.store.exceptions.ResourceAlreadyExistsException;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.ProductDiscountMapper;
import com.example.store.repositories.DiscountRepository;
import com.example.store.repositories.ProductDiscountRepository;
import com.example.store.repositories.ProductRepository;
import com.example.store.services.ProductDiscountService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductDiscountServiceImpl implements ProductDiscountService {

    @Autowired private ProductRepository productRepository;
    @Autowired private DiscountRepository discountRepository;
    @Autowired private ProductDiscountRepository productDiscountRepository;

    private final ProductDiscountMapper productDiscountMapper = Mappers.getMapper(ProductDiscountMapper.class);

    @Override
    public ResponseEntity<ResponseObject> createProductDiscount(Long productId, Long discountId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find discount with ID = " + discountId));

        Optional<ProductDiscount> getProductDiscount = productDiscountRepository.findProductDiscountByDiscountAndProduct(discount, product);
        if (getProductDiscount.isPresent()){
            throw new ResourceAlreadyExistsException("Discount of product is exists");
        }
        ProductDiscount productDiscount = new ProductDiscount();
        productDiscount.setDiscount(discount);
        productDiscount.setProduct(product);
        ProductDiscountResponseDTO productDiscountResponseDTO
                = productDiscountMapper.productDiscountToProductDiscountResponseDTO(productDiscountRepository.save(productDiscount));
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Create product discount success!!!", productDiscountResponseDTO));
    }

    @Override
    public ResponseEntity<?> getProductDiscount(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
        List<ProductDiscount> productDiscountList = productDiscountRepository.findProductDiscountByProduct(product);
        if (productDiscountList.size() == 0){
            throw new ResourceNotFoundException("Product hasn't discount!!!");
        }
        List<ProductDiscountResponseDTO> productDiscountResponseDTOList = new ArrayList<>();
        for (ProductDiscount productDiscount : productDiscountList){
            productDiscountResponseDTOList.add
                    (productDiscountMapper.productDiscountToProductDiscountResponseDTO(productDiscount));
        }
        return ResponseEntity.status(HttpStatus.OK).body(productDiscountResponseDTOList);
    }

    @Override
    public ResponseEntity<ResponseObject> softDeleteProductDiscount(Long productId, Long discountId, boolean deleted) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find discount with ID = " + discountId));

        ProductDiscount getProductDiscount = productDiscountRepository.findProductDiscountByDiscountAndProduct(discount, product).orElseThrow(
                () -> new ResourceNotFoundException("Could not find product discount with ID product = " + productId + " and ID discount = " + discount)
        );
        getProductDiscount.setDeleted(deleted);
        this.productDiscountRepository.save(getProductDiscount);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Delete product discount success!!!", deleted));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteProductDiscount(Long productId, Long discountId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find product with ID = " + productId));
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find discount with ID = " + discountId));

        ProductDiscount getProductDiscount = productDiscountRepository.findProductDiscountByDiscountAndProduct(discount, product).orElseThrow(
                () -> new ResourceNotFoundException("Could not find product discount with ID product = " + productId + " and ID discount = " + discount)
        );
        this.productDiscountRepository.delete(getProductDiscount);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Delete product discount success!!!"));
    }
}
