package com.example.storeserver.facade;

import com.example.storeserver.dto.PaymentDTO;
import com.example.storeserver.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentFacade {

    public PaymentDTO paymentToPaymentDTO(Payment payment) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(payment.getId());
        paymentDTO.setPaymentType(payment.getPaymentType());
        paymentDTO.setStatus(payment.getStatus());
        paymentDTO.setAmount(payment.getAmount());

        return paymentDTO;
    }

}
