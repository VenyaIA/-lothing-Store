package com.example.storeserver.services;

import com.example.storeserver.dto.ColorDTO;
import com.example.storeserver.entity.Color;
import com.example.storeserver.entity.Customer;
import com.example.storeserver.entity.Product;
import com.example.storeserver.entity.enums.EnumRole;
import com.example.storeserver.exceptions.ColorNotFoundException;
import com.example.storeserver.exceptions.ExceptionIsNotEnoughAuthority;
import com.example.storeserver.exceptions.ProductNotFoundException;
import com.example.storeserver.repositories.ColorRepository;
import com.example.storeserver.repositories.CustomerRepository;
import com.example.storeserver.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class ColorService {
    public static final Logger LOG = LoggerFactory.getLogger(ColorService.class);

    private final ColorRepository colorRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;


    @Autowired
    public ColorService(ColorRepository colorRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.colorRepository = colorRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    // api/color/create
    public Color createColor(ColorDTO colorDTO, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Color color = new Color();
            color.setName(colorDTO.getName());
            color.setHexCode(colorDTO.getHexCode());

            LOG.info("Saving color");
            return colorRepository.save(color);
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/color/all
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    // api/color/:colorId
    public Color getColorById(Long colorId) {
        return colorRepository.findById(colorId)
                .orElseThrow(() -> new ColorNotFoundException("Color cannot be found"));
    }

    //api/color/:colorId/:productId/add
    public Color addProductByIdInColor(Long colorId, Long productId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Color color = colorRepository.findById(colorId)
                    .orElseThrow(() -> new ColorNotFoundException("Color cannot be found"));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));

            color.getProducts().add(product);
            Color resultColor = colorRepository.save(color);

            product.getColors().add(resultColor);
            productRepository.save(product);

            return resultColor;
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/color/:colorId/:productId/delete
    public void deleteProductByIdFromColorById(Long colorId, Long productId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Color color = colorRepository.findById(colorId)
                    .orElseThrow(() -> new ColorNotFoundException("Color cannot be found"));

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));

            color.getProducts().remove(product);
            colorRepository.save(color);

            product.getColors().remove(color);
            productRepository.save(product);
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/color/:colorId/delete
    public void deleteColorById(Long colorId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Color color = getColorById(colorId);
            List<Product> products = color.getProducts();
            for (Product product : products) {
                product.getColors().remove(color);
                productRepository.save(product);
            }
            color.getProducts().clear();

            colorRepository.delete(color);
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/color/:productId/colors
    public List<Color> productByIdColors(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));
        return product.getColors();
    }

    private Customer getCustomerByPrincipal(Principal principal) {
        String username = principal.getName();
        return customerRepository.findCustomerByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
    }
}
