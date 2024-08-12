package com.shopme.admin.user.service;

import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {


    List<Role> listRoles();

    User saveUser(User user);

    boolean isEmailUnique(Long id, String email);

    void deleteUser(Long id) throws UserNotFoundException;

    User getById(Long id) throws UserNotFoundException;

    void updateUserEnabledStatus(Long id, Boolean enabled);

    List<User> listAll();

    Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyWord);
}
