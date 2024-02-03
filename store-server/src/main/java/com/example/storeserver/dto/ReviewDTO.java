package com.example.storeserver.dto;

import com.example.storeserver.entity.Product;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ReviewDTO {

    private Long id;
    @NotEmpty
    private String message;
    private String username;
    private Long userId;

}
