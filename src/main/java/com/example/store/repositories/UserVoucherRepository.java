package com.example.store.repositories;

import com.example.store.entities.User;
import com.example.store.entities.UserVoucher;
import com.example.store.entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserVoucherRepository extends JpaRepository<UserVoucher, Long> {
    List<UserVoucher> findUserVoucherByUser(User user);
    Optional<UserVoucher> findUserVoucherByUserAndVoucher(User user, Voucher voucher);

}
