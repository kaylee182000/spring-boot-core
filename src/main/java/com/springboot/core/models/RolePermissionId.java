package com.springboot.core.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class RolePermissionId implements Serializable {

    private Long roleId;
    private Long permissionId;
}
