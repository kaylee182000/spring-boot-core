package com.springboot.core.services;

import org.springframework.stereotype.Service;

import com.springboot.core.models.Permission;
import com.springboot.core.repositories.PermissionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public Permission getPermissionByName(String permissionName) {
        return permissionRepository.findPermissionsByName(permissionName);
    }
}
