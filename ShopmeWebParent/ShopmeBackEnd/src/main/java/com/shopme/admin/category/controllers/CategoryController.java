package com.shopme.admin.category.controllers;


import com.shopme.admin.category.service.CategoryService;
import com.shopme.admin.exception.CategoryNotFoundException;
import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
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
    public String listAll(@Param("sortDir") String sortDir, Model model) {
        if (sortDir == null || sortDir.isEmpty())
            sortDir = "asc";

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        List<Category> listCategories = categoryService.listAll(sortDir);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("reverseSortDir", reverseSortDir);

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
                               RedirectAttributes redirectAttributes) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);

            Category savedCategory = categoryService.save(category);
            String uploadDir = "../category-images/" + savedCategory.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
//            if (category.getImage().isEmpty()) category.setImage(null);
            categoryService.save(category);
        }




        redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");

        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id,
                             RedirectAttributes redirectAttributes, Model model) {
        try {
            categoryService.deleteCategory(id);
            String categoryDir = "../category-images/" + id;
            FileUploadUtil.removeDir(categoryDir);

            redirectAttributes.addFlashAttribute("message",
                    "The category ID " + id + " has been deleted successfully");

        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/categories";
    }



    @GetMapping("/edit/{id}")
    public String editCategory(Model model, @PathVariable Long id,
                               RedirectAttributes redirectAttributes) {

        try {
            Category category = categoryService.getCategoryById(id);
            List<Category> categories = categoryService.listCategoriesUsedInForm();
            model.addAttribute("category", category);
            model.addAttribute("pageTitle", "Edit Category (ID : " + id + ")");
            model.addAttribute("listCategories", categories);

            return "categories/category_form";
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/categories";
        }

    }


    @GetMapping("/{id}/enabled/{status}")
    public String enableCategory(@PathVariable("id") Long id, @PathVariable("status") boolean enabled,
                             RedirectAttributes redirectAttributes) {
        categoryService.updateCategoryEnabledStatus(id, enabled);
        String status = enabled ? "Enabled" : "Disabled";
        String message = "The category with ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }




}
