package com.example.store.repositories;

import com.example.store.entities.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    @Query(value = "select b from Banner b where :date between b.startDate and b.endDate and b.status = true ")
    List<Banner> findAllAvailableBanner(Date date);
}
