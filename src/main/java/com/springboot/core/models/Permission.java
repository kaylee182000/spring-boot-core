package com.springboot.core.models;

import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "permissions")
@Data
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deletedDate")
    private Date deletedDate;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "apis", nullable = false)
    private String apis;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Role> roles;
}
