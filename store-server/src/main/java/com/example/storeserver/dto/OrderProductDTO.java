package com.example.storeserver.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OrderProductDTO {

    private Long id;
    @NotEmpty
    private String status;


}
