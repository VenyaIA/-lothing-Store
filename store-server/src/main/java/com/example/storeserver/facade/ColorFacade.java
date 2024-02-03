package com.example.storeserver.facade;

import com.example.storeserver.dto.ColorDTO;
import com.example.storeserver.entity.Color;
import org.springframework.stereotype.Component;

@Component
public class ColorFacade {

    public ColorDTO colorToColorDTO(Color color) {
        ColorDTO colorDTO = new ColorDTO();
        colorDTO.setId(color.getId());
        colorDTO.setName(color.getName());
        colorDTO.setHexCode(color.getHexCode());

        return colorDTO;

    }

}
