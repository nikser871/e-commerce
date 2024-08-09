package com.shopme.admin.user.controller;


import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.user.service.UserService;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import static com.shopme.admin.constants.Constants.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String listAll(Model model) {
        List<User> users = userService.listAll();
        model.addAttribute("listUsers", users);
        return "users";
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
