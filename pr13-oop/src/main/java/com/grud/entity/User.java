package com.grud.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class User {
    private Integer id;

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private Role role;
}
