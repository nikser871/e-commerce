package com.shopme.admin.category.service;

import com.shopme.admin.exception.CategoryNotFoundException;
import com.shopme.common.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> listAll(String sortDir);

    List<Category> listCategoriesUsedInForm();

    Category save(Category category);

    Category getCategoryById(Long id) throws CategoryNotFoundException;

    String checkUnique(Long id, String name, String alias);

    void updateCategoryEnabledStatus(Long id, boolean enabled);

    void deleteCategory(Long id) throws CategoryNotFoundException;
}
