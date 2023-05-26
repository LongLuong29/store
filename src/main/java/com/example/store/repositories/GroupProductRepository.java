package com.example.store.repositories;

import com.example.store.entities.GroupProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupProductRepository extends JpaRepository<GroupProduct,Long> {
    Optional<GroupProduct> findGroupProductByName(String name);
}
