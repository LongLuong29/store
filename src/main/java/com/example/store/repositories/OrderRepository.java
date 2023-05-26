package com.example.store.repositories;

import com.example.store.entities.Order;
import com.example.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findOrdersByUser (User user);
}
