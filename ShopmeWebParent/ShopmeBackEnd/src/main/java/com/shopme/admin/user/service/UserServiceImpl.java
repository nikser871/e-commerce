package com.shopme.admin.user.service;


import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.user.RoleRepository;
import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        boolean isUpdatingUser = user.getId() != null;

        if (isUpdatingUser) {
            User existingUser = userRepository.findById(user.getId()).get();

            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }

        userRepository.save(user);
    }


    @Override
    public boolean isEmailUnique(Long id, String email) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) return true;

        boolean isCreatingNew = (id == null);

        if (isCreatingNew) {
            return false;
        } else return Objects.equals(user.getId(), id);
    }

    @Override
    public User getById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Could not find any user with ID " + id));
    }

    private void encodePassword(User user) {
        var encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
    }
}
