package com.springboot.core.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mapstruct.ReportingPolicy;

import com.springboot.core.dtos.RoleDto;
import com.springboot.core.models.Role;

@Mapper(componentModel = "spring", uses = PermissionMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mapping(source = "permissions", target = "rolePermissions")
    RoleDto roleToRoleDto(Role role);
}