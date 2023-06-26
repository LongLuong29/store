package com.example.store.repositories;

import com.example.store.entities.Order;
import com.example.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findOrdersByUser (User user);

  List<Order> findOrdersByStatus(String status);

  @Query(value = "SELECT o FROM Order o where o.createdDate >= :sinceDay and o.createdDate <=:toDay")
  List<Order> findOrderByDate(Date sinceDay, Date toDay);
//  @Modifying
//  @Query(value = "UPDATE Order o SET o.paidDate = :paidDate where o.id = :orderId")
//  int  updateOrderPaidDay(@Param("orderId") Long orderId,@Param("paidDate") Date paidDate);
}
