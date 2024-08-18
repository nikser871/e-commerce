package com.shopme.admin.category.service;

import com.shopme.admin.category.repositories.CategoryRepository;
import com.shopme.common.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoryList = categoryRepository.findAll();
        List<Category> categoriesUsedInForm = new ArrayList<>();
        for (Category category : categoryList) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(Category.builder().name(category.getName()).build());
                listChildren(category, 1, categoriesUsedInForm);
            }
        }

        return categoriesUsedInForm;

    }

    private void listChildren(Category root, int level, List<Category> categoriesUsedInForm) {
        for (Category child : root.getChildren()) {
            categoriesUsedInForm.add(Category.builder().name("--".repeat(level) + child.getName()).build());
            listChildren(child, level + 1, categoriesUsedInForm);
        }
    }

}
