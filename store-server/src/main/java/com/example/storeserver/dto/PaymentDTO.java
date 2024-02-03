package com.example.storeserver.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
public class PaymentDTO {

    private Long id;

    @NotEmpty
    private String paymentType;

    @NotEmpty
    private String status;

    private BigDecimal amount;

}
