package com.shopme.admin.user.service;


import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public List<User> listAll() {
        return userRepository.findAll();
    }
}
