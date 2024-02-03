package com.example.storeserver.dto;

import com.example.storeserver.entity.Product;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PromotionDTO {

    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotNull
    private Integer discountPercent;

}
