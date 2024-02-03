package com.example.storeserver.controllers;

import com.example.storeserver.dto.CartDTO;
import com.example.storeserver.entity.Cart;
import com.example.storeserver.facade.CartFacade;
import com.example.storeserver.payload.response.MessageResponse;
import com.example.storeserver.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("api/cart")
public class CartController {

    private final CartFacade cartFacade;
    private final CartService cartService;

    @Autowired
    public CartController(CartFacade cartFacade, CartService cartService) {
        this.cartFacade = cartFacade;
        this.cartService = cartService;
    }

    @GetMapping("/current")
    public ResponseEntity<CartDTO> getCurrentCart(Principal principal) {
        Cart cart = cartService.getCurrentCart(principal);
        CartDTO cartDTO = cartFacade.cartToCartDTO(cart);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PatchMapping("/clearCurrent")
    public ResponseEntity<MessageResponse> clearCart(Principal principal) {
        cartService.clearCart(principal);
        return new ResponseEntity<>(new MessageResponse("Cart was cleaned"), HttpStatus.OK);
    }

    @PatchMapping("/current/{productId}/delete")
    public ResponseEntity<MessageResponse> deleteProductByIdFromCurrentCart(Principal principal,
                                                                            @PathVariable("productId") String productId) {
        cartService.deleteProductByIdFromCurrentCart(Long.parseLong(productId), principal);

        return new ResponseEntity<>(new MessageResponse("Product was deleted from Cart"), HttpStatus.OK);
    }

    @PatchMapping("/current/{productId}/add")
    public ResponseEntity<CartDTO> addProductByIdInCurrentCart(Principal principal,
                                                               @PathVariable("productId") String productId) {
        Cart cart = cartService.addProductByIdInCurrentCart(principal, Long.parseLong(productId));
        CartDTO cartDTO = cartFacade.cartToCartDTO(cart);

        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }
}
