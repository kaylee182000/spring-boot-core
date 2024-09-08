package com.springboot.core.models;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "permissions")
@Data
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deletedDate", columnDefinition = "datetime(3)")
    private LocalDateTime deletedDate;

    @Column(name = "name", nullable = false, length = 191, unique = true)
    private String name;

    @Column(name = "apis", columnDefinition = "json")
    private String apis;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Role> roles;
}