package com.example.storeserver.controllers;

import com.example.storeserver.dto.CustomerDTO;
import com.example.storeserver.entity.Customer;
import com.example.storeserver.facade.CustomerFacade;
import com.example.storeserver.services.CustomerService;
import com.example.storeserver.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("api/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerFacade customerFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerFacade customerFacade, ResponseErrorValidation responseErrorValidation) {
        this.customerService = customerService;
        this.customerFacade = customerFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/")
    public ResponseEntity<Object> getCurrentCustomer(Principal principal) {

        Customer customer = customerService.getCurrentCustomer(principal);
        CustomerDTO customerDTO = customerFacade.customerToCustomerDTO(customer);

        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerProfile(@PathVariable("customerId") String customerId) {
        Customer customer = customerService.getCustomerById(Long.parseLong(customerId));
        CustomerDTO customerDTO = customerFacade.customerToCustomerDTO(customer);

        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<Object> updateCustomer(@Valid @RequestBody CustomerDTO customerDTO,
                                                 BindingResult bindingResult,
                                                 Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Customer customer = customerService.updateCustomer(customerDTO, principal);
        CustomerDTO customerUpdate = customerFacade.customerToCustomerDTO(customer);

        return new ResponseEntity<>(customerUpdate, HttpStatus.OK);
    }

}
