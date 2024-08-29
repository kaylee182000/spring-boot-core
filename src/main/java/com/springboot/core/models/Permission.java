package com.springboot.core.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    SUPER_ADMIN_READ("management:read"),
    SUPER_ADMIN_UPDATE("management:update"),
    SUPER_ADMIN_CREATE("management:create"),
    SUPER_ADMIN_DELETE("management:delete")

    ;

    @Getter
    private final String permission;
}
