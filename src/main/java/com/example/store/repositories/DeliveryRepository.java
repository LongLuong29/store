package com.example.store.repositories;

import com.example.store.entities.Order;
import com.example.store.entities.Delivery;
import com.example.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Delivery findDeliveryByOrder(Order order);

    List<Delivery> findDeliveriesByStatus(String status);
    List<Delivery> findDeliveriesByShipper(User shipper);

    List<Delivery> findDeliveriesByStatusAndShipper(String status, User shipper);

//
//    @Query(value = "select d from Delivery d where (d.shipper is null or d.shipper.id = :shipperId) and d.address.province = :province and d.status <>  'cancel' and d.status <> 'waiting'")
//    List<Delivery> getDeliveryByProvinceAndShipper(@Param("province") String province, @Param("shipperId") Long shipperId);
//
//    @Query(value = "select d from Delivery d where d.status = 'checked' and d.address.province = :province")
//    List<Delivery> findDeliveryByCheckedAndAddress(@Param("province") String province);
//
}
