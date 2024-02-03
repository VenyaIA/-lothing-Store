package com.example.storeserver.controllers;

import com.example.storeserver.dto.PaymentDTO;
import com.example.storeserver.entity.Payment;
import com.example.storeserver.facade.PaymentFacade;
import com.example.storeserver.services.PaymentService;
import com.example.storeserver.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PrePersist;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentFacade paymentFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentFacade paymentFacade, ResponseErrorValidation responseErrorValidation) {
        this.paymentService = paymentService;
        this.paymentFacade = paymentFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/{orderProductId}/create")
    public ResponseEntity<Object> createPaymentForOrderProductById(@Valid @RequestBody PaymentDTO paymentDTO,
                                                                   @PathVariable("orderProductId") String orderProductId,
                                                                   BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Payment payment = paymentService.createPayment(paymentDTO, Long.parseLong(orderProductId));
        PaymentDTO createdPayment = paymentFacade.paymentToPaymentDTO(payment);

        return new ResponseEntity<>(createdPayment, HttpStatus.OK);
    }

    @PatchMapping("/{orderProductId}/update")
    public ResponseEntity<Object> updateStatusPaymentForOrderProductById(@PathVariable("orderProductId") String orderProductId) {

        Payment payment = paymentService.updateStatusByOrderProductId(Long.parseLong(orderProductId));
        PaymentDTO updatedPayment = paymentFacade.paymentToPaymentDTO(payment);

        return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
    }
}
