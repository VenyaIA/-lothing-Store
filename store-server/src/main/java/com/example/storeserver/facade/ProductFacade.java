package com.example.storeserver.facade;

import com.example.storeserver.dto.ProductDTO;
import com.example.storeserver.entity.Product;
import com.example.storeserver.services.ColorService;
import com.example.storeserver.services.PromotionService;
import com.example.storeserver.services.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductFacade {

    private final ColorService colorService;
    private final SizeService sizeService;
    private final PromotionService promotionService;

    @Autowired
    public ProductFacade(ColorService colorService, SizeService sizeService, PromotionService promotionService) {
        this.colorService = colorService;
        this.sizeService = sizeService;
        this.promotionService = promotionService;
    }

    public ProductDTO productToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setTitle(product.getTitle());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setLikes(product.getLikes());
        productDTO.setBrand(product.getBrand());
        productDTO.setCategory(product.getCategory());
        productDTO.getColors().addAll(colorService.productByIdColors(product.getId()));
        productDTO.getSizes().addAll(sizeService.productByIdSizes(product.getId()));
        productDTO.getPromotions().addAll(promotionService.productByIdPromotions(product.getId()));

        return productDTO;
    }

}
