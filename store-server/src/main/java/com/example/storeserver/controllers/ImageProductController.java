package com.example.storeserver.controllers;

import com.example.storeserver.dto.ImageProductDTO;
import com.example.storeserver.entity.ImageProduct;
import com.example.storeserver.facade.ImageProductFacade;
import com.example.storeserver.payload.response.MessageResponse;
import com.example.storeserver.services.ImageProductService;
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
@RequestMapping("api/imageProduct")
public class ImageProductController {

    private final ImageProductService imageProductService;
    private final ImageProductFacade imageProductFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public ImageProductController(ImageProductService imageProductService, ImageProductFacade imageProductFacade, ResponseErrorValidation responseErrorValidation) {
        this.imageProductService = imageProductService;
        this.imageProductFacade = imageProductFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/{productId}/upload")
    public ResponseEntity<Object> uploadImageProduct(@Valid @RequestBody ImageProductDTO imageProductDTO,
                                                     @PathVariable String productId,
                                                     BindingResult bindingResult,
                                                     Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        ImageProduct imageProduct = imageProductService.uploadImageProductById(imageProductDTO, Long.parseLong(productId), principal);
        ImageProductDTO createdImageProduct = imageProductFacade.imageProductToImageProductDTO(imageProduct);

        return new ResponseEntity<>(createdImageProduct, HttpStatus.OK);
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<List<ImageProductDTO>> getAllImageProductById(@PathVariable("productId") String productId) {
        List<ImageProduct> imageProducts = imageProductService.getAllImagesProductById(Long.parseLong(productId));

        List<ImageProductDTO> imageProductDTOList = imageProducts.stream().map(imageProductFacade::imageProductToImageProductDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(imageProductDTOList, HttpStatus.OK);
    }

    @GetMapping("/{productId}/{imageId}")
    public ResponseEntity<ImageProductDTO> getImageProductByIdInProductById(@PathVariable("productId") String productId,
                                                                            @PathVariable("imageId") String imageId) {
        ImageProduct imageProduct = imageProductService
                .getImageProductByIdInProductById(Long.parseLong(productId), Long.parseLong(imageId));
        ImageProductDTO imageProductDTO = imageProductFacade.imageProductToImageProductDTO(imageProduct);

        return new ResponseEntity<>(imageProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/{imageId}/delete")
    public ResponseEntity<MessageResponse> deleteImageByIdInProductById(@PathVariable("productId") String productId,
                                                                        @PathVariable("imageId") String imageId,
                                                                        Principal principal) {
        imageProductService.deleteImageProductByIdInProductById(Long.parseLong(productId), Long.parseLong(imageId), principal);

        return new ResponseEntity<>(new MessageResponse("ImageProduct was deleted"), HttpStatus.OK);
    }
}
