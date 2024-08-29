package com.springboot.core.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "role_permissions")
@IdClass(RolePermissionId.class)
@Data
public class RolePermission {

    @Id
    private Long roleId;

    @Id
    private Long permissionId;
}
