package com.shopme.admin.category.service;

import com.shopme.common.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> listAll();
    List<Category> listCategoriesUsedInForm();

}
