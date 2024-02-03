package com.example.storeserver.controllers;

import com.example.storeserver.dto.ColorDTO;
import com.example.storeserver.dto.SizeDTO;
import com.example.storeserver.entity.Color;
import com.example.storeserver.entity.Size;
import com.example.storeserver.facade.SizeFacade;
import com.example.storeserver.payload.response.MessageResponse;
import com.example.storeserver.services.SizeService;
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
@RequestMapping("api/size")
public class SizeController {

    private final SizeService sizeService;
    private final SizeFacade sizeFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public SizeController(SizeService sizeService, SizeFacade sizeFacade, ResponseErrorValidation responseErrorValidation) {
        this.sizeService = sizeService;
        this.sizeFacade = sizeFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createSize(@Valid @RequestBody SizeDTO sizeDTO,
                                             BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Size size = sizeService.createSize(sizeDTO, principal);
        SizeDTO createdSize = sizeFacade.sizeToSizeDTO(size);

        return new ResponseEntity<>(createdSize, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SizeDTO>> getAllColors() {
        List<Size> sizes = sizeService.getAllSizes();
        List<SizeDTO> sizeDTOList = sizes.stream().map(sizeFacade::sizeToSizeDTO).collect(Collectors.toList());

        return new ResponseEntity<>(sizeDTOList, HttpStatus.OK);
    }

    @GetMapping("/{productId}/sizes")
    public ResponseEntity<List<SizeDTO>> productByIdSizes(@PathVariable("productId") String productId) {
        List<Size> sizes = sizeService.productByIdSizes(Long.parseLong(productId));
        List<SizeDTO> sizeDTOList = sizes.stream().map(sizeFacade::sizeToSizeDTO).collect(Collectors.toList());

        return new ResponseEntity<>(sizeDTOList, HttpStatus.OK);
    }

    @PatchMapping("/{sizeId}/{productId}/add")
    public ResponseEntity<SizeDTO> addProductByIdInSize(@PathVariable("sizeId") String sizeId,
                                                        @PathVariable("productId") String productId,
                                                        Principal principal) {
        Size size = sizeService
                .addProductByIdInSize(Long.parseLong(sizeId), Long.parseLong(productId), principal);
        SizeDTO sizeDTO = sizeFacade.sizeToSizeDTO(size);

        return new ResponseEntity<>(sizeDTO,HttpStatus.OK);
    }

    @PatchMapping("/{sizeId}/{productId}/delete")
    public ResponseEntity<MessageResponse> deleteProductByIdFromSizeById(@PathVariable("sizeId") String sizeId,
                                                          @PathVariable("productId") String productId,
                                                          Principal principal) {
        sizeService.deleteProductByIdFromSizeById(Long.parseLong(sizeId), Long.parseLong(productId), principal);

        return new ResponseEntity<>(new MessageResponse("Product was deleted from Size"), HttpStatus.OK);
    }

    @DeleteMapping("/{sizeId}/delete")
    public ResponseEntity<MessageResponse> deleteSizeById(@PathVariable("sizeId") String sizeId,
                                                          Principal principal) {
        sizeService.deleteSizeById(Long.parseLong(sizeId), principal);

        return new ResponseEntity<>(new MessageResponse("Size was deleted"), HttpStatus.OK);
    }
}
