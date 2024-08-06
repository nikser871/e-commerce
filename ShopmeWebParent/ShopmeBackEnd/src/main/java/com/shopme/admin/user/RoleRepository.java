package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface RoleRepository extends JpaRepository<Role, Long> {

}
