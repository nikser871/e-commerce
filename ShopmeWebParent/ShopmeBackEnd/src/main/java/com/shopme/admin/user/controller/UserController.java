package com.shopme.admin.user.controller;


import com.shopme.admin.user.service.UserService;
import com.shopme.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
