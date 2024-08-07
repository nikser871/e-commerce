package com.shopme.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncodeTest {

    @Test
    public void testPasswordEncode() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "nam2020";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(encoder.matches(rawPassword, encodedPassword)).isTrue();
    }
}
