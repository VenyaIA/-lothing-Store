package com.example.storeserver.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ColorDTO {

    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String hexCode;

}
