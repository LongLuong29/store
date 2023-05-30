package com.example.store.repositories;

import com.example.store.entities.Rank;
import com.example.store.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RankRepository extends JpaRepository<Rank, Long> {
    Optional<Rank> findRankByName(String name);
    Optional<Rank> findRankById(Long id);

}
