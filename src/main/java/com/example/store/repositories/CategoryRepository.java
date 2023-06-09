package com.example.store.repositories;

import com.example.store.entities.Category;
import com.example.store.entities.GroupProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findCategoryByName(String name);

    List<Category> findCategoriesByGroupProduct(GroupProduct groupProduct);

}
