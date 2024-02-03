package com.example.storeserver.facade;

import com.example.storeserver.dto.CategoryDTO;
import com.example.storeserver.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryFacade {

    public CategoryDTO categoryToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());

        return categoryDTO;
    }

}
