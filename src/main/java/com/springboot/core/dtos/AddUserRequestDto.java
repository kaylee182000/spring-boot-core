package com.springboot.core.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AddUserRequestDto {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String phone;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private Status status;

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
