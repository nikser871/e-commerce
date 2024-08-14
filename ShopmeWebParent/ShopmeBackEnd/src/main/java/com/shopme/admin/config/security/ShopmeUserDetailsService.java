package com.shopme.admin.config.security;

import com.shopme.admin.user.repositories.UserRepository;
import com.shopme.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopmeUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);

        if (user != null)
            return new ShopmeUserDetails(user);

        throw new UsernameNotFoundException("Could not find user with email: " + email);
    }
}
