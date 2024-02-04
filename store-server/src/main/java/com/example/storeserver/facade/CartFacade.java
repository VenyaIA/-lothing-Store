package com.example.storeserver.facade;

import com.example.storeserver.dto.CartDTO;
import com.example.storeserver.entity.Cart;
import org.springframework.stereotype.Component;

@Component
public class CartFacade {

    public CartDTO cartToCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setTotalPrice(cart.getTotalPrice().toString());

        return cartDTO;
    }

}
