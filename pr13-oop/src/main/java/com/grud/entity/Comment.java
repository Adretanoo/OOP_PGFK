package com.grud.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class Comment {
    private Integer id;

    @NotBlank
    private String content;

    @NotNull
    private Post post;
}
