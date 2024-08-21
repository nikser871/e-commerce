package com.shopme.admin.category;

import com.shopme.admin.category.repositories.CategoryRepository;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTests {

    private final CategoryRepository repo;


    @Autowired
    public CategoryRepositoryTests(CategoryRepository repo) {
        this.repo = repo;
    }

    @Test
    @Rollback(false)
    public void testCreateCategory() {
        Category category = Category.builder()
                .name("Electronics")
                .alias("Electronics")
                .image("default.png")
                .enabled(false)
                .build();

        Category saved = repo.save(category);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Electronics");
    }

    @Test
    @Rollback(false)
    public void testCreateSunCategory() {
        Category parent = repo.findById(7L).get();
        Category subCategory = Category.builder()
                .name("iphone")
                .alias("iphone")
                .image("default.png")
                .enabled(false)
                .build();
        subCategory.setParent(parent);
        Category saved = repo.save(subCategory);


        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("iphone");


    }

    @Test
    public void testGetCategory() {
        Category category = repo.findById(1L).get();
        List<Category> children = category.getChildren();

        System.out.println(category.getName());

        for (Category child : children) {
            System.out.println(child.getName());
        }


        assertThat(category.getChildren()).hasSizeGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategory() {
        List<Category> categories = repo.findAll();

        for (Category category : categories) {
            if (category.getParent() == null) {
                System.out.println(category.getName());
                printChildren(category, 1);
            }
        }
    }

    @Test
    public void testListRootCategories() {
        List<Category> categories = repo.findRootCategories(Sort.by("name").ascending());

        categories.forEach(System.out::println);

        assertThat(categories).hasSizeGreaterThan(0);

    }


    private void printChildren(Category root, int level) {
        for (Category child : root.getChildren()) {
            System.out.println("--".repeat(level) + child.getName());
            printChildren(child, level + 1);
        }
    }

    @Test
    public void testFindByName() {
        String name = "Computers";

        Category byName = repo.findByName(name);

        assertThat(byName).isNotNull();
        assertThat(byName.getName()).isEqualTo(name);
    }

    @Test
    public void testFindByAlias() {
        String name = "computers";

        Category byAlias = repo.findByName(name);

        assertThat(byAlias).isNotNull();
        assertThat(byAlias.getAlias()).isEqualTo(name);
    }



}
