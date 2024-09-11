package com.springboot.core.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.core.models.Permission;
import com.springboot.core.models.Role;
import com.springboot.core.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Optional<Role> findRoleByEmail(String email) {
        return roleRepository.findRoleByEmail(email);
    }

    public List<String> findMenuByRole(Long roleId) {
        return roleRepository.findMenuByRole(roleId);
    }

    public Role getRoleWithPermissions(Long roleId) {
        return roleRepository.findRoleWithPermissions(roleId);
    }

    public List<Permission> findPermissionByRoleId(Long roleId) {
        List<Permission> list = roleRepository.findPermissionByRoleId(roleId);
        for (Permission permission : list) {
            permission.setApis(
                    permission.getApis().stream().map(jsonNode -> {
                        try {
                            return objectMapper.treeToValue(jsonNode, JsonNode.class);
                        } catch (JsonProcessingException e) {
                            // Handle the exception here, e.g. log the error or throw a custom exception
                            // You can also return a default value or null if appropriate
                            return null;
                        }
                    })
                            .collect(Collectors.toList()));
        }
        return list;
    }
}
