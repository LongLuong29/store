package com.example.store.repositories;

import com.example.store.entities.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
  Optional<Attribute> findAttributeByNameAndValue(String name, String value);
  Optional<Attribute> findAttributeByName(String name);
}
