package com.springboot.core.models;

import java.util.Date;
import java.util.Set;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FetchType;
import jakarta.persistence.FieldResult;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "permissions")
@Data
@SqlResultSetMapping(name = "PermissionMapping", entities = @EntityResult(entityClass = Permission.class, fields = {
        @FieldResult(name = "id", column = "id"),
        @FieldResult(name = "deletedDate", column = "deletedDate"),
        @FieldResult(name = "name", column = "name"),
        @FieldResult(name = "apis", column = "apis")
}))
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deletedDate")
    private Date deletedDate;

    @Column(name = "name", nullable = false, length = 191, unique = true)
    private String name;

    @Column(name = "apis", columnDefinition = "json")
    private String apis;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Role> roles;
}