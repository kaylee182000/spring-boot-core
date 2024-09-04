package com.springboot.core.models;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.lang.Collections;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private Long createdBy;

    @Column(name = "updatedDate")
    private LocalDate updatedDate;

    @Column(name = "updatedBy")
    private Long updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deletedDate")
    private Date deletedDate;

    @Column(name = "activatedDate")
    private LocalDate activatedDate;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "hash")
    private String hash;

    @Column(name = "phone")
    private String phone;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "roleId", nullable = false)
    private Role role;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "middleName")
    private String middleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "publisherId")
    private Long publisherId;

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