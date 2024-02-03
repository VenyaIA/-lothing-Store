package com.example.storeserver.dto;

import com.example.storeserver.entity.*;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDTO {

    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotEmpty
    private String price;
    private Integer likes;
    private Brand brand;
    private Category category;
    private List<Color> colors = new ArrayList<>();
    private List<Size> sizes = new ArrayList<>();
    private List<Promotion> promotions = new ArrayList<>();

}
