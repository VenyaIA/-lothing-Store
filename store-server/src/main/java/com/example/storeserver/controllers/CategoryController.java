package com.example.storeserver.controllers;

import com.example.storeserver.dto.CategoryDTO;
import com.example.storeserver.entity.Category;
import com.example.storeserver.facade.CategoryFacade;
import com.example.storeserver.payload.response.MessageResponse;
import com.example.storeserver.services.CategoryService;
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
@RequestMapping("api/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryFacade categoryFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryFacade categoryFacade, ResponseErrorValidation responseErrorValidation) {
        this.categoryService = categoryService;
        this.categoryFacade = categoryFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                 BindingResult bindingResult,
                                                 Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Category category = categoryService.createCategory(categoryDTO, principal);
        CategoryDTO createdCategory = categoryFacade.categoryToCategoryDTO(category);

        return new ResponseEntity<>(createdCategory, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOList = categories.stream().map(categoryFacade::categoryToCategoryDTO).collect(Collectors.toList());
        return new ResponseEntity<>(categoryDTOList, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("categoryId") String categoryId) {
        Category category = categoryService.getCategoryById(Long.parseLong(categoryId));
        CategoryDTO categoryDTO = categoryFacade.categoryToCategoryDTO(category);

        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @PatchMapping("/{categoryId}/{productId}/add")
    public ResponseEntity<CategoryDTO> addProductByIdInCategory(@PathVariable("categoryId") String categoryId,
                                                          @PathVariable("productId") String productId,
                                                          Principal principal) {
        Category category = categoryService
                .addProductByIdInCategory(Long.parseLong(categoryId), Long.parseLong(productId), principal);
        CategoryDTO categoryDTO = categoryFacade.categoryToCategoryDTO(category);

        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}/delete")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable("categoryId") String categoryId,
                                                          Principal principal) {
        categoryService.deleteCategory(Long.parseLong(categoryId), principal);
        return new ResponseEntity<>(new MessageResponse("Category was deleted"), HttpStatus.OK);
    }
}
