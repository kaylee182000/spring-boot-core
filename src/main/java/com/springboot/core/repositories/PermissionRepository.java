package com.springboot.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.core.models.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query(value = "SELECT * " +
            "FROM permissions " +
            "WHERE name = :permissionName", nativeQuery = true)
    Permission findPermissionsByName(@Param("permissionName") String permissionName);
}
