package com.shopme.admin.category.service;

import com.shopme.admin.category.repositories.CategoryRepository;
import com.shopme.admin.exception.CategoryNotFoundException;
import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.common.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.shopme.admin.util.Util.*;


@Service
@RequiredArgsConstructor
@Qualifier("primary")
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listAll(String sortDir) {
        Sort sort = Sort.by("name");

        if (sortDir.equals("asc"))
            sort = sort.ascending();
        else if (sortDir.equals("desc"))
            sort = sort.descending();


        List<Category> rootCategories = categoryRepository.findRootCategories(sort);
        return listHierarchicalCategories(rootCategories, sortDir);
    }

    @Override
    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoryList = categoryRepository.findRootCategories(Sort.by("name").ascending());
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

    @Override
    public void updateCategoryEnabledStatus(Long id, boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }

    @Override
    public void deleteCategory(Long id) throws CategoryNotFoundException {
        var countById = categoryRepository.countById(id);
        if (countById == 0) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }

        categoryRepository.deleteById(id);
    }

    private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category category : rootCategories) {
            hierarchicalCategories.add(copyFull(category));
            listSubHierarchicalCategories(category, 1, hierarchicalCategories, sortDir);
        }

        return hierarchicalCategories;

    }

    private void listSubCategoriesUsedInForm(Category root, int level, List<Category> categoriesUsedInForm) {
        for (Category child : sortSubCategories(root.getChildren(), "asc")) {
            categoriesUsedInForm.add(Category.builder()
                    .id(child.getId())
                    .name("--".repeat(level) + child.getName()).build());
            listSubCategoriesUsedInForm(child, level + 1, categoriesUsedInForm);
        }
    }

    private void listSubHierarchicalCategories(Category root, int level, List<Category> categoriesUsedInForm,
                                               String sortDir) {
        for (Category child : sortSubCategories(root.getChildren(), sortDir)) {
            categoriesUsedInForm.add(copyFullWithName(child, "--".repeat(level) + child.getName()));
            listSubHierarchicalCategories(child, level + 1, categoriesUsedInForm, sortDir);
        }
    }

    private List<Category> sortSubCategories(List<Category> subCategories, String sortDir) {

        List<Category> categories = new ArrayList<>(subCategories);
        if (sortDir == null || sortDir.equals("asc") || sortDir.isEmpty())
            Collections.sort(categories, Comparator.comparing(Category::getName));
        else if (sortDir.equals("desc"))
            Collections.sort(categories, Comparator.comparing(Category::getName).reversed());

        return categories;
    }

}
