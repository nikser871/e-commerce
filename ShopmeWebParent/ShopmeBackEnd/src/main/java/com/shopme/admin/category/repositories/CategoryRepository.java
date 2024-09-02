package com.shopme.admin.category.repositories;


import com.shopme.common.entity.Category;
import org.apache.poi.ss.formula.functions.Count;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, PagingAndSortingRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.parent.id IS NULL ORDER BY c.name")
    List<Category> findRootCategories(Sort sort);

    Category findByName(String name);

    Category findByAlias(String alias);

    @Modifying
    @Query("UPDATE Category c SET c.enabled = ?2 WHERE c.id = ?1")
    void updateEnabledStatus(Long id, Boolean enabled);

    Long countById(Long id);
}
