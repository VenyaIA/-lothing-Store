package com.example.storeserver.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ImageProductDTO {

    @NotEmpty
    private String url;
}
