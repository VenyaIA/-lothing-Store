package com.example.storeserver.dto;

import com.example.storeserver.entity.Product;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class SizeDTO {

    private Long id;
    @NotEmpty
    private String title;

}
