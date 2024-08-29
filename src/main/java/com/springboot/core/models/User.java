package com.springboot.core.models;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "created_by")
    private Long createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_date")
    private Date deletedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activated_date", nullable = false)
    private Date activatedDate;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "hash")
    private String hash;

    @Column(name = "phone")
    private String phone;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "middle_name")
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

    @Column(name = "publisher_id")
    private Long publisherId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return null; // You will need to implement this method to return the user's authorities
                     // (roles)
    }

    public enum Sex {
        MALE,
        FEMALE,
        OTHER
    }

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}