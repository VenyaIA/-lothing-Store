package com.example.storeserver.dto;

import com.example.storeserver.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Игнорируем прокси-свойства Hibernate
public class OrderProductDTO {

    private Long id;
    @NotEmpty
    private String status;


}
