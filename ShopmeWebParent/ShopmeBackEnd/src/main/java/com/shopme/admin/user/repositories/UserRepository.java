package com.shopme.admin.user.repositories;

import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {

    User getUserByEmail(String email);

    Long countById(Long id);

    @Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) LIKE %?1%")
    Page<User> findAll(String keyword, Pageable pageable);

    @Modifying
    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    void updateEnabledStatus(Long id, Boolean enabled);

    Page<User> findAll(Pageable pageable);


}
