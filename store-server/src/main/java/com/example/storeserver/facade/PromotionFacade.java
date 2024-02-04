package com.example.storeserver.facade;

import com.example.storeserver.dto.PromotionDTO;
import com.example.storeserver.entity.Promotion;
import org.springframework.stereotype.Component;

@Component
public class PromotionFacade {

    public PromotionDTO promotionToPromotionDTO(Promotion promotion) {
        PromotionDTO promotionDTO = new PromotionDTO();
        promotionDTO.setId(promotion.getId());
        promotionDTO.setTitle(promotion.getTitle());
        promotionDTO.setDescription(promotion.getDescription());
        promotionDTO.setDiscountPercent(promotion.getDiscountPercent());

        return promotionDTO;
    }

}
