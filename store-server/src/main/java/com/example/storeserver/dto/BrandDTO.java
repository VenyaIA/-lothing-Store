package com.example.storeserver.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class BrandDTO {

    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;

}
