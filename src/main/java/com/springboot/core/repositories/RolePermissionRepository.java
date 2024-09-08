package com.springboot.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.core.models.RolePermission;
import com.springboot.core.models.RolePermission.RolePermissionId;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {

}
