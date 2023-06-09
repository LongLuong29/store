package com.example.store.repositories;

import com.example.store.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Optional<Voucher> findVoucherByCode(String code);
}
