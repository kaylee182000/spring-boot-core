package com.springboot.core.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.core.models.Permission;
import com.springboot.core.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

        @Query("SELECT r FROM Role r JOIN FETCH r.permissions WHERE r.id = :roleId")
        Role findRoleWithPermissions(@Param("roleId") Long roleId);

        @Query("SELECT p FROM Permission p JOIN p.roles r WHERE r.id = :roleId")
        List<Permission> findPermissionByRoleId(@Param("roleId") Long roleId);

        @Query(value = "SELECT mr.menuId " +
                        "FROM menu_roles mr " +
                        "WHERE mr.roleId = :roleId", nativeQuery = true)
        public List<String> findMenuByRole(@Param("roleId") Long roleId);

        @Query(value = "SELECT r " +
                        "FROM Role r JOIN FETCH r.users u " +
                        "WHERE u.email = :email")
        Optional<Role> findRoleByEmail(@Param("email") String email);
}