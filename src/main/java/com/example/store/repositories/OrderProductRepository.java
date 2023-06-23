package com.example.store.repositories;

import com.example.store.entities.Order;
import com.example.store.entities.OrderProduct;
import com.example.store.entities.Product;
import com.example.store.models.IProductQuantity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
  List<OrderProduct> findOrderProductByOrder(Order order);

  List<OrderProduct> findOrderProductByProduct(Product product);

  Optional<OrderProduct> findOrderProductByOrderAndProduct(Order order, Product product);

//  @Query(value = "select sum (b.quantity) from OrderProduct b where b.order.status = 'Done'")
//  Optional<Integer> numberProductOfAllOrder();
//
//  @Query(value = "select b.quantity as product, sum (b.quantity) as quantity from OrderProduct b" +
//          " where b.order.status = 'Done' group by b.product")
//  Page<IProductQuantity> numberProductOfOrder(Pageable pageable);
}
