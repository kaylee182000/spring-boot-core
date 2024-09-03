package com.springboot.core.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.springboot.core.models.Role;
import com.springboot.core.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Optional<Role> findRoleCodeName(String email) {
        return roleRepository.findRoleCodeName(email);
    }

    public List<String> findMenuByRole(Long roleId) {
        return roleRepository.findMenuByRole(roleId);
    }
}
