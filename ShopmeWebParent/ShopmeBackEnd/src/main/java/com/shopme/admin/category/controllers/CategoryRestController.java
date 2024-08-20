package com.shopme.admin.category.controllers;

import com.shopme.admin.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryRestController {


    private final CategoryService categoryService;

    @PostMapping("/check_unique")
    public String checkUnique(@Param("id") Long id,
                              @Param("name") String name,
                              @Param("alias") String alias) {
        return categoryService.checkUnique(id, name, alias);
    }

}

