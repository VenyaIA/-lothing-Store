package com.example.storeserver.controllers;

import com.example.storeserver.dto.PromotionDTO;
import com.example.storeserver.entity.Promotion;
import com.example.storeserver.facade.PromotionFacade;
import com.example.storeserver.payload.response.MessageResponse;
import com.example.storeserver.services.PromotionService;
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
@RequestMapping("/api/promotion")
public class PromotionController {

    private final PromotionService promotionService;
    private final PromotionFacade promotionFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public PromotionController(PromotionService promotionService, PromotionFacade promotionFacade, ResponseErrorValidation responseErrorValidation) {
        this.promotionService = promotionService;
        this.promotionFacade = promotionFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPromotion(@Valid @RequestBody PromotionDTO promotionDTO,
                                                  BindingResult bindingResult,
                                                  Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Promotion promotion = promotionService.createPromotion(promotionDTO, principal);
        PromotionDTO createdPromotion = promotionFacade.promotionToPromotionDTO(promotion);

        return new ResponseEntity<>(createdPromotion, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PromotionDTO>> getAllColors() {
        List<Promotion> promotions = promotionService.getAllPromotions();
        List<PromotionDTO> promotionDTOList = promotions.stream().map(promotionFacade::promotionToPromotionDTO).collect(Collectors.toList());

        return new ResponseEntity<>(promotionDTOList, HttpStatus.OK);
    }

    @GetMapping("/{productId}/promotions")
    public ResponseEntity<List<PromotionDTO>> productByIdSizes(@PathVariable("productId") String productId) {
        List<Promotion> promotions = promotionService.productByIdPromotions(Long.parseLong(productId));
        List<PromotionDTO> promotionDTOList = promotions.stream().map(promotionFacade::promotionToPromotionDTO).collect(Collectors.toList());

        return new ResponseEntity<>(promotionDTOList, HttpStatus.OK);
    }

    @PatchMapping("/{promotionId}/{productId}/add")
    public ResponseEntity<PromotionDTO> addProductByIdInPromotion(@PathVariable("promotionId") String promotionId,
                                                                @PathVariable("productId") String productId,
                                                                Principal principal) {
        Promotion promotion = promotionService
                .addProductByIdInPromotion(Long.parseLong(promotionId), Long.parseLong(productId), principal);
        PromotionDTO promotionDTO = promotionFacade.promotionToPromotionDTO(promotion);

        return new ResponseEntity<>(promotionDTO, HttpStatus.OK);
    }

    @PatchMapping("/{promotionId}/{productId}/delete")
    public ResponseEntity<MessageResponse> deleteProductByIdFromPromotionById(@PathVariable("promotionId") String promotionId,
                                                                              @PathVariable("productId") String productId,
                                                                              Principal principal) {
        promotionService.deleteProductByIdFromPromotionById(Long.parseLong(promotionId),
                Long.parseLong(productId), principal);

        return new ResponseEntity<>(new MessageResponse("Product was deleted from Promotion"), HttpStatus.OK);
    }

    @DeleteMapping("/{promotionId}/delete")
    public ResponseEntity<MessageResponse> deletePromotionById(@PathVariable("promotionId") String promotionId,
                                                               Principal principal) {
        promotionService.deletePromotionById(Long.parseLong(promotionId), principal);

        return new ResponseEntity<>(new MessageResponse("Promotion was deleted"), HttpStatus.OK);
    }

}
