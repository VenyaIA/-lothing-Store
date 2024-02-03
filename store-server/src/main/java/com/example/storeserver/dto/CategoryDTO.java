package com.example.storeserver.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CategoryDTO {

    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;

}
