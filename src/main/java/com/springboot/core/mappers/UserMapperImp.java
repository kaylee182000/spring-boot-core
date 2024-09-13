package com.springboot.core.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.core.dtos.PermissionDto;
import com.springboot.core.dtos.RoleDto;
import com.springboot.core.dtos.UserDto;
import com.springboot.core.models.Permission;
import com.springboot.core.models.Role;
import com.springboot.core.models.User;
import com.springboot.core.services.RoleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserMapperImp implements UserMapper {

    private final RoleService roleService;

    @Override
    public UserDto userToUserDto(User user) {
        Role role = user.getRole();
        if (role == null) {
            UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);
            return userDto;
        }
        RoleDto roleDto = RoleMapper.INSTANCE.roleToRoleDto(role);
        List<Permission> permissions = roleService.findPermissionByRoleId(role.getId());
        List<PermissionDto> listPermissionDto = new ArrayList<>();
        for (Permission permission : permissions) {
            PermissionDto permissionDto = PermissionMapper.INSTANCE.permissionToPermissionDto(permission);

            listPermissionDto.add(permissionDto);

        }
        roleDto.setRolePermissions(listPermissionDto);

        UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);
        userDto.setRole(roleDto);
        return userDto;
    }

}
