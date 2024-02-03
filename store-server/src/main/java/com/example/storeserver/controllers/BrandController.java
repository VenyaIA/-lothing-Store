package com.example.storeserver.controllers;

import com.example.storeserver.dto.BrandDTO;
import com.example.storeserver.entity.Brand;
import com.example.storeserver.facade.BrandFacade;
import com.example.storeserver.payload.response.MessageResponse;
import com.example.storeserver.services.BrandService;
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
@RequestMapping("api/brand")
public class BrandController {

    private final BrandService brandService;
    private final BrandFacade brandFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public BrandController(BrandService brandService, BrandFacade brandFacade, ResponseErrorValidation responseErrorValidation) {
        this.brandService = brandService;
        this.brandFacade = brandFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createBrand(@Valid @RequestBody BrandDTO brandDTO,
                                              BindingResult bindingResult,
                                              Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Brand brand = brandService.createBrand(brandDTO, principal);
        BrandDTO createdBrand = brandFacade.brandToBrandDTO(brand);

        return new ResponseEntity<>(createdBrand, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BrandDTO>> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        List<BrandDTO> brandDTOList = brands.stream().map(brandFacade::brandToBrandDTO).collect(Collectors.toList());
        return new ResponseEntity<>(brandDTOList, HttpStatus.OK);
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<BrandDTO> getBrandById(@PathVariable("brandId") String brandId) {
        Brand brand = brandService.getBrandById(Long.parseLong(brandId));
        BrandDTO brandDTO = brandFacade.brandToBrandDTO(brand);

        return new ResponseEntity<>(brandDTO, HttpStatus.OK);
    }

    @PatchMapping("/{brandId}/{productId}/add")
    public ResponseEntity<BrandDTO> addProductByIdInBrand(@PathVariable("brandId") String brandId,
                                                          @PathVariable("productId") String productId,
                                                          Principal principal) {
        Brand brand = brandService
                .addProductByIdInBrand(Long.parseLong(brandId), Long.parseLong(productId), principal);
        BrandDTO brandDTO = brandFacade.brandToBrandDTO(brand);

        return new ResponseEntity<>(brandDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{brandId}/delete")
    public ResponseEntity<MessageResponse> deleteBrandById(@PathVariable("brandId") String brandId, Principal principal) {
        brandService.deleteBrandById(Long.parseLong(brandId), principal);
        return new ResponseEntity<>(new MessageResponse("Brand was deleted"), HttpStatus.OK);
    }
}
