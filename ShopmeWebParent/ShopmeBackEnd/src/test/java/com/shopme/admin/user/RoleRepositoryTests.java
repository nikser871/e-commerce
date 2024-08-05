package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTests {

    @Autowired
    RoleRepository roleRepository;

    @Test
    @Rollback()
    public void testCreateFirstRole() {
        Role role = Role.builder()
                .name("ADMIN")
                .description("Manage everything")
                .build();

        Role savedRole = roleRepository.save(role);


        assertThat(savedRole.getId()).isGreaterThan(0);
        assertThat(savedRole.getName()).isEqualTo("ADMIN");
        assertThat(savedRole.getDescription()).isEqualTo("Manage everything");


    }

    @Test
    @Rollback()
    public void testCreateRestRoles() {

        Role salesPerson = Role.builder()
                .name("SalesPerson")
                .description("Manage product price, customers, shipping, orders and sales report")
                .build();

        Role editor = Role.builder()
                .name("Editor")
                .description("Manage categories, brands, products, articles and menus")
                .build();

        Role shipper = Role.builder()
                .name("Shipper")
                .description("View products, orders and update order status")
                .build();

        Role assistant  = Role.builder()
                .name("Assistant")
                .description("Manage questions and reviews")
                .build();

        roleRepository.saveAll(List.of(salesPerson, editor, shipper, assistant));
    }

}
