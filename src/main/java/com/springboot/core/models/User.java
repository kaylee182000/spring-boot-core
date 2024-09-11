package com.springboot.core.models;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.lang.Collections;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdDate", nullable = false)
    private Date createdDate;

    @Column(name = "createdBy")
    private Integer createdBy;

    @Column(name = "updatedDate", columnDefinition = "datetime(3)")
    private Date updatedDate;

    @Column(name = "updatedBy")
    private Integer updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deletedDate")
    private Date deletedDate;

    @Column(name = "activatedDate")
    private Date activatedDate;

    @Column(name = "email", nullable = false, length = 191)
    private String email;

    @Column(name = "hash", length = 191)
    private String hash;

    @Column(name = "phone", length = 191)
    private String phone;

    @Column(name = "firstName", length = 191)
    private String firstName;

    @Column(name = "lastName", length = 191)
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId")
    private Role role;

    @Column(name = "avatar", length = 191)
    private String avatar;

    @Column(name = "middleName", length = 191)
    private String middleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", columnDefinition = "enum('MALE','FEMALE','OTHER')")
    private Sex sex;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "address", length = 191)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "enum('ACTIVE','INACTIVE') default 'ACTIVE'")
    private Status status;

    @Column(name = "publisherId")
    private Integer publisherId;

    // Enums for sex and status
    public enum Sex {
        MALE,
        FEMALE,
        OTHER
    }

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return hash;
    }

    @Override
    public String getUsername() {
        return email;
    }
}