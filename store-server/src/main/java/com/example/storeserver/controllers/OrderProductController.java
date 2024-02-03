package com.example.storeserver.controllers;

import com.example.storeserver.dto.OrderProductDTO;
import com.example.storeserver.entity.OrderProduct;
import com.example.storeserver.facade.OrderProductFacade;
import com.example.storeserver.payload.response.MessageResponse;
import com.example.storeserver.services.OrderProductService;
import com.example.storeserver.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("api/order")
public class OrderProductController {

    private final OrderProductService orderProductService;
    private final OrderProductFacade orderProductFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public OrderProductController(OrderProductService orderProductService, OrderProductFacade orderProductFacade, ResponseErrorValidation responseErrorValidation) {
        this.orderProductService = orderProductService;
        this.orderProductFacade = orderProductFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createOrderProduct(@Valid @RequestBody OrderProductDTO orderProductDTO,
                                                     Principal principal,
                                                     BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;


        OrderProduct orderProduct = orderProductService.createOrderProduct(orderProductDTO, principal);
        OrderProductDTO createdOrderProduct = orderProductFacade.orderProductToOrderProductDTO(orderProduct);

        return new ResponseEntity<>(createdOrderProduct, HttpStatus.OK);
    }

    @GetMapping("/customer/{orderId}")
    public ResponseEntity<OrderProductDTO> getOrderProductByIdForCustomer(@PathVariable("orderId") String orderId,
                                                                          Principal principal) {
        OrderProduct orderProduct = orderProductService.getOrderProductByIdForCustomer(Long.parseLong(orderId), principal);
        OrderProductDTO orderProductDTO = orderProductFacade.orderProductToOrderProductDTO(orderProduct);

        return new ResponseEntity<>(orderProductDTO, HttpStatus.OK);
    }

    @GetMapping("/customer/all")
    public ResponseEntity<List<OrderProductDTO>> getOrderProductByCurrentCustomer(Principal principal) {
        List<OrderProduct> orderProducts = orderProductService.getAllOrderProductForCustomer(principal);
        List<OrderProductDTO> orderProductDTOList = orderProducts.stream().map(orderProductFacade::orderProductToOrderProductDTO).collect(Collectors.toList());

        return new ResponseEntity<>(orderProductDTOList, HttpStatus.OK);
    }

    @PatchMapping("/customer/{orderId}/{productId}/add")
    public ResponseEntity<OrderProductDTO> addProductByIdInOrderProductById(@PathVariable("orderId") String orderId,
                                                                            @PathVariable("productId") String productId,
                                                                            Principal principal) {
        OrderProduct orderProduct = orderProductService
                .addProductByIdInOrderProductById(Long.parseLong(orderId), Long.parseLong(productId), principal);
        OrderProductDTO orderProductDTO = orderProductFacade.orderProductToOrderProductDTO(orderProduct);

        return new ResponseEntity<>(orderProductDTO, HttpStatus.OK);
    }

    @PatchMapping("/customer/{orderId}/{productId}/delete")
    public ResponseEntity<MessageResponse> deleteProductByIdFromOrderProductById(@PathVariable("orderId") String orderId,
                                                                                 @PathVariable("productId") String productId,
                                                                                 Principal principal) {
        orderProductService
                .deleteProductByIdFromOrderProductById(Long.parseLong(orderId), Long.parseLong(productId), principal);

        return new ResponseEntity<>(new MessageResponse("Product was deleted from OrderProduct"), HttpStatus.OK);
    }

    @DeleteMapping("/customer/{orderId}/delete")
    public ResponseEntity<MessageResponse> deleteOrderProductById(@PathVariable("orderId") String orderId,
                                                                  Principal principal) {
        orderProductService.deleteOrderProductById(Long.parseLong(orderId), principal);

        return new ResponseEntity<>(new MessageResponse("OrderProduct was deleted"), HttpStatus.OK);
    }
}
