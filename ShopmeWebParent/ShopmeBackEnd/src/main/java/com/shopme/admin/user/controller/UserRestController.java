package com.shopme.admin.user.controller;


import com.shopme.admin.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserRestController {

    private final UserService userService;

    @PostMapping("/users/check_email")
    public String checkDuplicateEmail(String email) {
        return userService.isEmailUnique(email) ? "OK" : "Duplicated";
    }

}
