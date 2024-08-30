package com.springboot.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.core.models.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

}
