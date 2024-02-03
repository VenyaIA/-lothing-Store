package com.example.storeserver.services;

import com.example.storeserver.dto.CartDTO;
import com.example.storeserver.entity.*;
import com.example.storeserver.entity.enums.EnumRole;
import com.example.storeserver.exceptions.CartNotFoundException;
import com.example.storeserver.exceptions.ColorNotFoundException;
import com.example.storeserver.exceptions.ExceptionIsNotEnoughAuthority;
import com.example.storeserver.exceptions.ProductNotFoundException;
import com.example.storeserver.repositories.CartRepository;
import com.example.storeserver.repositories.CustomerRepository;
import com.example.storeserver.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

/**
 * api/cart/create – POST create new Cart
 * api/cart/:cartId/clear – POST delete Cart
 * api/cart/cartId – GET cart data
 */

@Service
public class CartService {
    public static final Logger LOG = LoggerFactory.getLogger(CartService.class);


    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CustomerRepository customerRepository, CartRepository cartRepository, ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    // api/cart/create
    public Cart createCart(Customer customer) {

        Cart cart = new Cart();
        cart.setCustomer(customer);
        cart.setTotalPrice(0.0);

        Cart resultCart = cartRepository.save(cart);
        customer.setCart(resultCart);
        customerRepository.save(customer);

        LOG.info("Saving cart");
        return resultCart;
    }

    // api/cart/current
    public Cart getCurrentCart(Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        return customer.getCart();
    }

    // api/cart/clearCurrent
    public void clearCart(Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        Cart cart = customer.getCart();

        List<Product> products = cart.getProducts();
        for (Product product : products) {
            product.getCarts().remove(cart);
            productRepository.save(product);
        }
        cart.setTotalPrice(0.0);
        cart.getProducts().clear();

        cartRepository.save(cart);
    }

    // api/cart/current/:product/add
    public Cart addProductByIdInCurrentCart(Principal principal, Long productId) {
        Customer customer = getCustomerByPrincipal(principal);
        Cart cart = customer.getCart();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));

        cart.getProducts().add(product);
        Double totalPrice = cart.getTotalPrice();
        if (!product.getPromotions().isEmpty()) {
            double discountAmount = Double.parseDouble(product.getPrice());
            for (Promotion promotion : product.getPromotions()) {
                discountAmount = (discountAmount * promotion.getDiscountPercent()) / 100;
            }
            totalPrice += Double.parseDouble(product.getPrice()) - discountAmount;
        } else {
            totalPrice += Double.parseDouble(product.getPrice());
        }
        cart.setTotalPrice(totalPrice);
        Cart resultCart = cartRepository.save(cart);

        product.getCarts().add(cart);
        productRepository.save(product);

        return resultCart;
    }

    // api/cart/current/:productId/delete
    public void deleteProductByIdFromCurrentCart(Long productId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        Cart cart = customer.getCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));

        cart.getProducts().remove(product);
        Double totalPrice = cart.getTotalPrice();
        if (!product.getPromotions().isEmpty()) {
            double discountAmount = Double.parseDouble(product.getPrice());
            for (Promotion promotion : product.getPromotions()) {
                discountAmount = (discountAmount * promotion.getDiscountPercent()) / 100;
            }
            totalPrice -= Double.parseDouble(product.getPrice()) - discountAmount;
        } else {
            totalPrice -= Double.parseDouble(product.getPrice());
        }
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);

        product.getCarts().remove(cart);
        productRepository.save(product);
    }

    private Customer getCustomerByPrincipal(Principal principal) {
        String username = principal.getName();
        return customerRepository.findCustomerByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
    }
}
