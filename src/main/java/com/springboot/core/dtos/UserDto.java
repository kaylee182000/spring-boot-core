package com.springboot.core.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;

    private String createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedDate;

    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date activatedDate;

    private String status;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String avatar;
    private String middleName;
    private String sex;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String address;
    private Long roleId;
    private Long publisherId;
    private RoleDto role;
}
