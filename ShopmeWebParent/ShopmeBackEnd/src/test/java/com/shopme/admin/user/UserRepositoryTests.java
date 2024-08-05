package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRule() {
        Role roleAdmin = entityManager.find(Role.class, 1L);
        User userNamHM = User.builder()
                .email("nam@codejava.net")
                .password("nam2020")
                .firstName("Nam")
                .lastName("Ha Minh")
                .build();

        userNamHM.addRole(roleAdmin);

        User savedUser = userRepository.save(userNamHM);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0L);
        assertThat(savedUser.getEmail()).isEqualTo("nam@codejava.net");
        assertThat(savedUser.getPassword()).isEqualTo("nam2020");
        assertThat(savedUser.getFirstName()).isEqualTo("Nam");
        assertThat(savedUser.getLastName()).isEqualTo("Ha Minh");
        assertThat(savedUser.getRoles().size()).isEqualTo(1);

    }

    @Test
    public void testCreateNewUserWithTwoRules() {
        User userRavi = User.builder()
                .email("ravi@gmail.com")
                .password("ravi2020")
                .firstName("Ravi")
                .lastName("Kumar")
                .build();


        Role editor = Role.builder()
                .id(3L)
                .build();

        Role assistant = Role.builder()
                .id(5L)
                .build();

        userRavi.addRole(editor);
        userRavi.addRole(assistant);
        User user = userRepository.save(userRavi);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isGreaterThan(0L);
        assertThat(user.getRoles()).hasSize(2);
        assertThat(user.getRoles()).contains(editor, assistant);

    }

    @Test
    public void testListAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();

        users.forEach(System.out::println);

        assertThat(users.size()).isEqualTo(2);

    }

    @Test
    public void testFindUserById() {
        User user = userRepository.findById(1L).get();
        assertThat(user).isNotNull();
        assertThat(user.getRoles()).hasSize(1);
    }

    @Test
//    @Rollback(false)
    public void testUpdateUserDetails() {
        User user = userRepository.findById(1L).get();

        user.setEnabled(true);
        user.setEmail("namjavaprogrammer@gmail.com");
        userRepository.save(user);

        User updatedUser = userRepository.findById(1L).get();

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getEmail()).isEqualTo("namjavaprogrammer@gmail.com");
        assertThat(updatedUser.isEnabled()).isEqualTo(true);


    }

    @Test
    @Rollback(false)
    public void testUpdateUserRoles() {
        User user = userRepository.findById(2L).get();
        Role salesPerson = Role.builder().id(2L).build();

        user.getRoles().remove(Role.builder().id(3L).build());
        user.getRoles().add(salesPerson);

        userRepository.save(user);

    }

    @Test
    public void testDeleteUser() {
        userRepository.deleteById(2L);

//        assertThrows(NullPointerException.class, () -> userRepository.findById(2L).get());
    }



}
