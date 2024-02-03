package com.example.storeserver.facade;

import com.example.storeserver.dto.ImageProductDTO;
import com.example.storeserver.entity.ImageProduct;
import org.springframework.stereotype.Component;

@Component
public class ImageProductFacade {

    public ImageProductDTO imageProductToImageProductDTO(ImageProduct imageProduct) {
        ImageProductDTO imageProductDTO = new ImageProductDTO();
        imageProductDTO.setUrl(imageProduct.getUrl());

        return imageProductDTO;
    }

}
