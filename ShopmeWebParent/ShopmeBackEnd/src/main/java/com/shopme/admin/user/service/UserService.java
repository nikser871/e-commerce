package com.shopme.admin.user.service;

import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> listAll();

    List<Role> listRoles();

    void saveUser(User user);

    boolean isEmailUnique(Long id, String email);

    void deleteUser(Long id) throws UserNotFoundException;

    User getById(Long id) throws UserNotFoundException;
}
