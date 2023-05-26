package com.example.store.repositories;

import com.example.store.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findAddressByProvinceAndDistrictAndWardAndApartmentNumber(
            String province,
            String district,
            String ward,
            String apartmentNumber
    );
}
