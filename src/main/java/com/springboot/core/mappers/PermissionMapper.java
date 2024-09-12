package com.springboot.core.mappers;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.core.dtos.PermissionDto;
import com.springboot.core.models.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);
    ObjectMapper objectMapper = new ObjectMapper();

    @Mapping(target = "apis", source = "apis", qualifiedByName = "mapApis")
    PermissionDto permissionToPermissionDto(Permission permission);

    @Named("mapApis")
    default List<String> mapApis(String apis) throws JsonMappingException, JsonProcessingException {
        List<String> listApis = objectMapper.readValue(apis, new TypeReference<List<String>>() {
        });
        List<String> newApis = new ArrayList<String>();
        for (String api : listApis) {
            newApis.add(api);
        }
        return newApis;
    }
}