package com.shopme.admin.user.controller;


import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.user.service.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


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
    public String saveUser(User user, RedirectAttributes redirectAttributes) {
        userService.saveUser(user);

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


}
