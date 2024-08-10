package com.shopme.admin.user.service;


import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.user.repositories.RoleRepository;
import com.shopme.admin.user.repositories.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.shopme.admin.constants.Constants.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
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

        return userRepository.save(user);
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
    public void deleteUser(Long id) throws UserNotFoundException {
        var countById = userRepository.countById(id);
        if (countById == 0) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }

        userRepository.deleteById(id);

    }

    @Override
    public User getById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Could not find any user with ID " + id));
    }

    @Override
    public void updateUserEnabledStatus(Long id, Boolean enabled) {
        userRepository.updateEnabledStatus(id, enabled);
    }

    @Override
    public Page<User> listByPage(int pageNum, String sortField, String sortDir) {

        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
        return userRepository.findAll(pageable);
    }

    private void encodePassword(User user) {
        var encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
    }
}
