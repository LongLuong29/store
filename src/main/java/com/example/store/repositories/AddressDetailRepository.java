package com.example.store.repositories;

import com.example.store.entities.Address;
import com.example.store.entities.AddressDetail;
import com.example.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AddressDetailRepository extends JpaRepository<AddressDetail, Long> {

    @Query("select ad.address from AddressDetail ad where ad.user.id= ?1")
    List<Address> findAddressByUser(Long id);

    @Query("select ad.user from AddressDetail ad where ad.address.id= ?1")
    List<User> findUserByAddress(Long id);

    List<AddressDetail> findAddressDetailsByUser(User user);

    Optional<AddressDetail> findAddressDetailByUserAndAddress(
            User user,
            Address address
    );

}
