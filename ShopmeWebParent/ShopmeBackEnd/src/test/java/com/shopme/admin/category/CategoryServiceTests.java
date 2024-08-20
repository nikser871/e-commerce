package com.shopme.admin.category;

import com.shopme.admin.category.repositories.CategoryRepository;
import com.shopme.admin.category.service.CategoryServiceImpl;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;


    @Test
    public void testCheckUniquenessOfNewModeCategoryName() {
        Long id = null;
        String name = "Computers";
        String alias = "something";
        Category category = Category.builder().id(id).name(name).alias(alias).build();

        when(categoryRepository.findByName(name)).thenReturn(category);
        when(categoryRepository.findByAlias(alias)).thenReturn(null);

        String result = categoryService.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateName");

    }

    @Test
    public void testCheckUniquenessOfNewModeCategoryAlias() {
        Long id = null;
        String name = "ABC";
        String alias = "computers";
        Category category = Category.builder().id(id).name(name).alias(alias).build();

        when(categoryRepository.findByName(name)).thenReturn(null);
        when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryService.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateAlias");

    }

    @Test
    public void testCheckUniquenessOfNewModeCategoryReturnOK() {
        Long id = null;
        String name = "ABC";
        String alias = "ABC";

        when(categoryRepository.findByName(name)).thenReturn(null);
        when(categoryRepository.findByAlias(alias)).thenReturn(null);

        String result = categoryService.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("OK");

    }

    @Test
    public void testCheckUniquenessOfEditModeCategoryName() {
        Long id = 1L;
        String name = "Computers";
        String alias = "something";
        Category category = Category.builder().id(2L).name(name).alias(alias).build();

        when(categoryRepository.findByName(name)).thenReturn(category);
        when(categoryRepository.findByAlias(alias)).thenReturn(null);

        String result = categoryService.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateName");

    }


    @Test
    public void testCheckUniquenessOfEditModeCategoryAlias() {
        Long id = 1L;
        String name = "ABC";
        String alias = "computers";
        Category category = Category.builder().id(2L).name(name).alias(alias).build();

        when(categoryRepository.findByName(name)).thenReturn(null);
        when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryService.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateAlias");

    }

    @Test
    public void testCheckUniquenessOfEditModeCategoryReturnOK() {
        Long id = 1L;
        String name = "ABC";
        String alias = "ABC";

        Category category = Category.builder().id(id).name(name).alias(alias).build();


        when(categoryRepository.findByName(name)).thenReturn(null);
        when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryService.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("OK");

    }

}
