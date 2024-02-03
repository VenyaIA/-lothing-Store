package com.example.storeserver.facade;

import com.example.storeserver.dto.BrandDTO;
import com.example.storeserver.entity.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandFacade {

    public BrandDTO brandToBrandDTO(Brand brand) {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(brand.getId());
        brandDTO.setTitle(brand.getTitle());
        brandDTO.setDescription(brand.getDescription());

        return brandDTO;
    }

}
