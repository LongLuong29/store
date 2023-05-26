package com.example.store.services.implement;

import com.example.store.dto.response.ResponseObject;
import com.example.store.services.OrderProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderProductServiceImpl implements OrderProductService {
    @Override
    public ResponseEntity<ResponseObject> addProductToOrder(Long orderId, Long productId, int amount) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject> deleteProductToOrder(Long orderId, Long productId) {
        return null;
    }

    @Override
    public ResponseEntity<?> getProductByOrder(Long orderId) {
        return null;
    }
}
