package com.springboot.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.core.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
