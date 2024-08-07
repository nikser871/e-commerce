package com.shopme.admin.user.service;


import com.shopme.admin.user.RoleRepository;
import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> listAll() {
        return userRepository.findAll();
    }

    @Override
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        encodePassword(user);
        userRepository.save(user);
    }

    @Override
    public boolean isEmailUnique(String email) {
        return userRepository.getUserByEmail(email) == null;
    }

    private void encodePassword(User user) {
        var encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
    }
}
