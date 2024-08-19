package com.shopme.admin.category.controllers;


import com.shopme.admin.category.service.CategoryService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Category;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String listAll(Model model) {
        List<Category> listCategories = categoryService.listAll();
        model.addAttribute("listCategories", listCategories);

        return "categories/categories";

    }

    @GetMapping("/new")
    public String newCategory(Model model) {

        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");

        return "categories/category_form";
    }

    @PostMapping("/save")
    public String saveCategory(Category category,
                               @RequestParam("fileImage") MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        category.setImage(fileName);
        Category saved = categoryService.save(category);

        String uploadDir = "../category-images/" + saved.getId();
        try {
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

            redirectAttributes.addFlashAttribute("message",
                    "Category saved successfully!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/categories";

    }

}
