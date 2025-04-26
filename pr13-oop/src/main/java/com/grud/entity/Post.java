package com.grud.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Post {
    private Integer id;

    @NotBlank
    private String title;

    private String content;

    @NotNull
    private User user;
}
