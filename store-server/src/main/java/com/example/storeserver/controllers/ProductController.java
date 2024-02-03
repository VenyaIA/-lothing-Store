package com.example.storeserver.controllers;

import com.example.storeserver.dto.ProductDTO;
import com.example.storeserver.entity.Product;
import com.example.storeserver.facade.ProductFacade;
import com.example.storeserver.payload.response.MessageResponse;
import com.example.storeserver.services.ProductService;
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
@RequestMapping("api/product")
public class ProductController {

    private final ProductService productService;
    private final ProductFacade productFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public ProductController(ProductService productService, ProductFacade productFacade, ResponseErrorValidation responseErrorValidation) {
        this.productService = productService;
        this.productFacade = productFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductDTO productDTO,
                                                BindingResult bindingResult,
                                                Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Product product = productService.createProduct(productDTO, principal);
        ProductDTO createdProduct = productFacade.productToProductDTO(product);

        return new ResponseEntity<>(createdProduct, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDTO> productDTOList = products.stream().map(productFacade::productToProductDTO).collect(Collectors.toList());

        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("productId") String productId) {
        Product product = productService.getProductById(Long.parseLong(productId));
        ProductDTO productDTO = productFacade.productToProductDTO(product);

        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<MessageResponse> deleteProductById(@PathVariable("productId") String productId,
                                                             Principal principal) {
        productService.deleteProduct(Long.parseLong(productId), principal);
        return new ResponseEntity<>(new MessageResponse("Product was deleted"), HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<Object> updateProductById(@Valid @RequestBody ProductDTO productDTO,
                                                    BindingResult bindingResult,
                                                    Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Product product = productService.updateProduct(productDTO, principal);
        ProductDTO productUpdate = productFacade.productToProductDTO(product);
        return new ResponseEntity<>(productUpdate, HttpStatus.OK);
    }

    @GetMapping("/{brandId}/productsByBrand")
    public ResponseEntity<List<ProductDTO>> getAllProductByBrand(@PathVariable("brandId") String brandId) {
        List<Product> products = productService.getAllProductsByBrand(Long.parseLong(brandId));
        List<ProductDTO> productDTOList = products.stream().map(productFacade::productToProductDTO).collect(Collectors.toList());

        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/productsByCategory")
    public ResponseEntity<List<ProductDTO>> getAllProductsByCategory(@PathVariable("categoryId") String categoryId) {
        List<Product> products = productService.getAllProductsByCategory(Long.parseLong(categoryId));
        List<ProductDTO> productDTOList = products.stream().map(productFacade::productToProductDTO).collect(Collectors.toList());

        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    @GetMapping("/customerCart/products")
    public ResponseEntity<List<ProductDTO>> getAllProductsByCustomerCart(Principal principal) {
        List<Product> products = productService.getAllProductsByCustomerCart(principal);
        List<ProductDTO> productDTOList = products.stream().map(productFacade::productToProductDTO).collect(Collectors.toList());

        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    @GetMapping("/{orderProductId}/products")
    public ResponseEntity<List<ProductDTO>> getAllProductsForOrderProductById(@PathVariable("orderProductId") String orderProductId) {
        List<Product> products = productService.getAllProductsForOrderProductById(Long.parseLong(orderProductId));
        List<ProductDTO> productDTOList = products.stream().map(productFacade::productToProductDTO).collect(Collectors.toList());

        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

}
