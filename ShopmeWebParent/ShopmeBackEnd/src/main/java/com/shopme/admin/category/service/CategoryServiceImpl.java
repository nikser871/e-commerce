package com.shopme.admin.category.service;

import com.shopme.admin.category.repositories.CategoryRepository;
import com.shopme.admin.exception.CategoryNotFoundException;
import com.shopme.common.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.shopme.admin.util.Util.*;


@Service
@RequiredArgsConstructor
@Qualifier("primary")
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listAll() {
        List<Category> rootCategories = categoryRepository.findRootCategories();
        return listHierarchicalCategories(rootCategories);
    }

    @Override
    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoryList = categoryRepository.findAll();
        List<Category> categoriesUsedInForm = new ArrayList<>();
        for (Category category : categoryList) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(Category.builder()
                        .id(category.getId())
                        .name(category.getName()).build());
                listSubCategoriesUsedInForm(category, 1, categoriesUsedInForm);
            }
        }

        return categoriesUsedInForm;

    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) throws CategoryNotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Could not find any category with ID " + id));

    }

    @Override
    public String checkUnique(Long id, String name, String alias) {
        boolean isCreatingNew = (id == null || id == 0);

        Category categoryByName = categoryRepository.findByName(name);
        Category categoryByAlias = categoryRepository.findByAlias(alias);

        if (isCreatingNew && categoryByName != null) {
            return "DuplicateName";
        } else if (isCreatingNew && categoryByAlias != null) {
            return "DuplicateAlias";
        } else if (!isCreatingNew && categoryByName != null
                && !categoryByName.getId().equals(id)) {
            return "DuplicateName";
        } else if (!isCreatingNew && categoryByAlias != null
                && !categoryByAlias.getId().equals(id)) {
            return "DuplicateAlias";
        }

        return "OK";
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category category : rootCategories) {
            hierarchicalCategories.add(copyFull(category));
            listSubHierarchicalCategories(category, 1, hierarchicalCategories);
        }

        return hierarchicalCategories;

    }

    private void listSubCategoriesUsedInForm(Category root, int level, List<Category> categoriesUsedInForm) {
        for (Category child : root.getChildren()) {
            categoriesUsedInForm.add(Category.builder()
                    .id(child.getId())
                    .name(child.getName()).build());
            listSubCategoriesUsedInForm(child, level + 1, categoriesUsedInForm);
        }
    }

    private void listSubHierarchicalCategories(Category root, int level, List<Category> categoriesUsedInForm) {
        for (Category child : root.getChildren()) {
            categoriesUsedInForm.add(copyFullWithName(child, "--".repeat(level) + child.getName()));
            listSubHierarchicalCategories(child, level + 1, categoriesUsedInForm);
        }
    }

}
