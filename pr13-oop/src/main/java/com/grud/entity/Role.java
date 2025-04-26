package com.grud.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Role {
    private Integer id;

    @NotBlank
    private String name;
}
