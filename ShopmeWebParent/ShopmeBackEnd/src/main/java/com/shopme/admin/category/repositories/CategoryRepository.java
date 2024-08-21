package com.shopme.admin.category.repositories;


import com.shopme.common.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>, PagingAndSortingRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.parent.id IS NULL ORDER BY c.name")
    List<Category> findRootCategories(Sort sort);

    Category findByName(String name);

    Category findByAlias(String alias);
}
