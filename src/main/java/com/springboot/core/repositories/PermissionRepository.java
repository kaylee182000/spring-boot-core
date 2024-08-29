package com.springboot.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.core.models.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
