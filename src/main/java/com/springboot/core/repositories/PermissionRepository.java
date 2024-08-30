package com.springboot.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.core.models.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query(value = "SELECT * " +
            "FROM permissions p JOIN role_permissions rp ON p.id = rp.permissionId WHERE rp.roleId = :roleId", nativeQuery = true)
    List<Permission> findPermissionsByRoleId(@Param("roleId") Long roleId);
}
