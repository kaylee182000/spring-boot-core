package com.springboot.core.models;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.springboot.core.models.Permission.ADMIN_CREATE;
import static com.springboot.core.models.Permission.ADMIN_DELETE;
import static com.springboot.core.models.Permission.ADMIN_READ;
import static com.springboot.core.models.Permission.ADMIN_UPDATE;
import static com.springboot.core.models.Permission.SUPER_ADMIN_CREATE;
import static com.springboot.core.models.Permission.SUPER_ADMIN_DELETE;
import static com.springboot.core.models.Permission.SUPER_ADMIN_READ;
import static com.springboot.core.models.Permission.SUPER_ADMIN_UPDATE;

@RequiredArgsConstructor
public enum RoleName {
        USER(Collections.emptySet()),
        SUPER_ADMIN(
                        Set.of(
                                        ADMIN_READ,
                                        ADMIN_UPDATE,
                                        ADMIN_DELETE,
                                        ADMIN_CREATE,
                                        SUPER_ADMIN_READ,
                                        SUPER_ADMIN_UPDATE,
                                        SUPER_ADMIN_DELETE,
                                        SUPER_ADMIN_CREATE)),
        ADMIN(
                        Set.of(
                                        ADMIN_READ,
                                        ADMIN_UPDATE,
                                        ADMIN_DELETE,
                                        ADMIN_CREATE))

        ;

        @Getter
        private final Set<Permission> permissions;

        public List<SimpleGrantedAuthority> getAuthorities() {
                var authorities = getPermissions()
                                .stream()
                                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                                .collect(Collectors.toList());
                authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
                return authorities;
        }
}
