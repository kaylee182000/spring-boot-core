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

    @Mapping(target = "apis", qualifiedByName = "mapApis")
    PermissionDto permissionToPermissionDto(Permission permission);

    @Named("mapApis")
    default List<String> mapApis(List<JsonNode> apis) {
        // Implement the logic to map List<JsonNode> to List<String>
        // For example:
        return apis.stream()
                .map(JsonNode::asText)
                .collect(Collectors.toList());
    }
}