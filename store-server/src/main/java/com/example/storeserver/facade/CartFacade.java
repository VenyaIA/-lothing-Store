package com.example.storeserver.facade;

import com.example.storeserver.dto.CartDTO;
import com.example.storeserver.entity.Cart;
import com.example.storeserver.entity.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartFacade {

    public CartDTO cartToCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setTotalPrice(cart.getTotalPrice().toString());

        return cartDTO;
    }

}
