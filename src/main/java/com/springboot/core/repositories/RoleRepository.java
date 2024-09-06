package com.springboot.core.repositories;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.core.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
        @Query(value = "SELECT r " +
                        "FROM Role r JOIN FETCH r.users u " +
                        "WHERE u.email = :email")
        Optional<Role> findRoleByEmail(@Param("email") String email);

        @Query(value = "SELECT mr.menuId " +
                        "FROM menu_roles mr " +
                        "WHERE mr.roleId = :roleId", nativeQuery = true)
        public List<String> findMenuByRole(@Param("roleId") Long roleId);

        @Query(value = "SELECT r.id,r.code,r.createdBy,r.createdDate,r.deletedDate,r.name,rp.roleId,p1_1.id,p1_1.apis,p1_1.deletedDate,p1_1.name,r.recoveryConfigId,r.updatedBy,r.updatedDate "
                        +
                        "FROM roles r left join roles_permissions rp on r.id=rp.roleId left join permissions p1_1 on p1_1.id=rp.permissionId  "
                        +
                        "WHERE r.id= :roleId", nativeQuery = true)
        List<Map<String, Object>> findPermissionByRoleId(@Param("roleId") Long roleId);

        @Query(value = "SELECT r FROM " +
                        " Role r JOIN FETCH r.permissions " +
                        " WHERE r.id = :roleId")
        Role findRoleWithPermissions(@Param("roleId") Long roleId);

}
