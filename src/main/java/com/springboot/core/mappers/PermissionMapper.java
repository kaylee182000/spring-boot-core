package com.springboot.core.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.core.dtos.PermissionDto;
import com.springboot.core.models.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);
    ObjectMapper objectMapper = new ObjectMapper();

    @Mapping(target = "apis", source = "apis")
    PermissionDto permissionToPermissionDto(Permission permission);
}