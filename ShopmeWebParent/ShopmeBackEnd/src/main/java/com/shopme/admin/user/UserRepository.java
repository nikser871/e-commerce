package com.shopme.admin.user;

import com.shopme.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;



public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByEmail(String email);

    Long countById(Long id);

    @Modifying
    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    void updateEnabledStatus(Long id, Boolean enabled);



}
