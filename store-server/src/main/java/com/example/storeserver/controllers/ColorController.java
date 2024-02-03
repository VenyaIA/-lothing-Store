package com.example.storeserver.controllers;

import com.example.storeserver.dto.ColorDTO;
import com.example.storeserver.entity.Color;
import com.example.storeserver.facade.ColorFacade;
import com.example.storeserver.payload.response.MessageResponse;
import com.example.storeserver.services.ColorService;
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
@RequestMapping("api/color")
public class ColorController {

    private final ColorService colorService;
    private final ColorFacade colorFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public ColorController(ColorService colorService, ColorFacade colorFacade, ResponseErrorValidation responseErrorValidation) {
        this.colorService = colorService;
        this.colorFacade = colorFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createColor(@Valid @RequestBody ColorDTO colorDTO,
                                              BindingResult bindingResult,
                                              Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Color color = colorService.createColor(colorDTO, principal);
        ColorDTO createdColor = colorFacade.colorToColorDTO(color);

        return new ResponseEntity<>(createdColor, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ColorDTO>> getAllColors() {
        List<Color> colors = colorService.getAllColors();
        List<ColorDTO> colorDTOList = colors.stream().map(colorFacade::colorToColorDTO).collect(Collectors.toList());

        return new ResponseEntity<>(colorDTOList, HttpStatus.OK);
    }

    @GetMapping("/{colorId}")
    public ResponseEntity<ColorDTO> getColorById(@PathVariable("colorId") String colorId) {
        Color color = colorService.getColorById(Long.parseLong(colorId));
        ColorDTO colorDTO = colorFacade.colorToColorDTO(color);

        return new ResponseEntity<>(colorDTO, HttpStatus.OK);
    }

    @GetMapping("/{productId}/colors")
    public ResponseEntity<List<ColorDTO>> productByIdColors(@PathVariable("productId") String productId) {
        List<Color> colors = colorService.productByIdColors(Long.parseLong(productId));
        List<ColorDTO> colorDTOList = colors.stream().map(colorFacade::colorToColorDTO).collect(Collectors.toList());

        return new ResponseEntity<>(colorDTOList, HttpStatus.OK);
    }

    @PatchMapping("/{colorId}/{productId}/add")
    public ResponseEntity<ColorDTO> addProductByIdInColor(@PathVariable("colorId") String colorId,
                                                          @PathVariable("productId") String productId,
                                                          Principal principal) {
        Color color = colorService
                .addProductByIdInColor(Long.parseLong(colorId), Long.parseLong(productId), principal);
        ColorDTO colorDTO = colorFacade.colorToColorDTO(color);

        return new ResponseEntity<>(colorDTO, HttpStatus.OK);
    }

    @PatchMapping("/{colorId}/{productId}/delete")
    public ResponseEntity<MessageResponse> deleteProductByIdFromColorById(@PathVariable("colorId") String colorId,
                                                                          @PathVariable("productId") String productId,
                                                                          Principal principal) {
        colorService.deleteProductByIdFromColorById(Long.parseLong(colorId), Long.parseLong(productId), principal);

        return new ResponseEntity<>(new MessageResponse("Product was deleted from Color"), HttpStatus.OK);
    }

    @DeleteMapping("/{colorId}/delete")
    public ResponseEntity<MessageResponse> deleteColorById(@PathVariable("colorId") String colorId,
                                                           Principal principal) {
        colorService.deleteColorById(Long.parseLong(colorId), principal);

        return new ResponseEntity<>(new MessageResponse("Color was deleted"), HttpStatus.OK);
    }

}
