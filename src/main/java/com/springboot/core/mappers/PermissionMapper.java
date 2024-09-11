package com.springboot.core.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.fasterxml.jackson.databind.JsonNode;
import com.springboot.core.dtos.PermissionDto;
import com.springboot.core.models.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    PermissionDto permissionToPermissionDto(Permission permission);
}