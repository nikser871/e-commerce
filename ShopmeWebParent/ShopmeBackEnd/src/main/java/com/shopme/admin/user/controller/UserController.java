package com.shopme.admin.user.controller;


import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.user.service.UserService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static com.shopme.admin.constants.Constants.USERS_PER_PAGE;


@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String listFirstPage(Model model) {
        return listByPage(1, model, "firstName", "asc", null);
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        User user = new User();
        user.setEnabled(true);
        List<Role> roles = userService.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", roles);
        model.addAttribute("pageTitle", "Create New User");

        return "user_form";
    }

    @GetMapping("/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum,
                             Model model,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir,
                             @Param("keyWord") String keyWord) {

        Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyWord);
        List<User> users = page.getContent();

        long startCount = (long) (pageNum - 1) * USERS_PER_PAGE + 1;
        long endCount = startCount + USERS_PER_PAGE - 1;

        if (endCount > page.getTotalElements()) endCount = page.getTotalElements();

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currPage", pageNum);
        model.addAttribute("lastPage", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("listUsers", users);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyWord", keyWord);


        return "users";
    }


    @PostMapping("/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.saveUser(user);

            String uploadDir = "user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty()) user.setPhotos(null);

            userService.saveUser(user);
        }


        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id,
                           RedirectAttributes redirectAttributes, Model model) {

        try {
            User user = userService.getById(id);
            List<Role> roles = userService.listRoles();
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User (ID : " + id + ")");
            model.addAttribute("listRoles", roles);

            return "user_form";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }


    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id,
                             RedirectAttributes redirectAttributes, Model model) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message",
                    "The user with ID " + id + " has been deleted successfully.");

        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/{id}/enabled/{status}")
    public String enableUser(@PathVariable("id") Long id, @PathVariable("status") boolean enabled,
                             RedirectAttributes redirectAttributes) {
        userService.updateUserEnabledStatus(id, enabled);
        String status = enabled ? "Enabled" : "Disabled";
        String message = "The user with ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/users";
    }


}
