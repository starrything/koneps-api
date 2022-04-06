package com.jonghyun.koneps.global.security.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey> {
    List<UserRole> findRolesByUser_username(String username);

    List<UserRole> findByRole(String role);

    void deleteByUsername(String username);
}
