package com.example.store.repositories;

import com.example.store.entities.Order;
import com.example.store.entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
  List<OrderProduct> findOrderProductByOrder(Order bill);
}
