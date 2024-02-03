package com.example.storeserver.services;

import com.example.storeserver.dto.PromotionDTO;
import com.example.storeserver.entity.*;
import com.example.storeserver.entity.enums.EnumRole;
import com.example.storeserver.exceptions.ExceptionIsNotEnoughAuthority;
import com.example.storeserver.exceptions.ProductNotFoundException;
import com.example.storeserver.exceptions.PromotionNotFoundException;
import com.example.storeserver.repositories.CustomerRepository;
import com.example.storeserver.repositories.ProductRepository;
import com.example.storeserver.repositories.PromotionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * api/promotion/create – POST create new Promotion
 * api/promotion/:promotionId/delete – POST delete Promotion
 * api/promotion/:product – GET product promotion data
 */

@Service
public class PromotionService {
    public static final Logger LOG = LoggerFactory.getLogger(PromotionService.class);

    private final PromotionRepository promotionRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.promotionRepository = promotionRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    // api/promotion/create
    public Promotion createPromotion(PromotionDTO promotionDTO, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Promotion promotion = new Promotion();
            promotion.setTitle(promotionDTO.getTitle());
            promotion.setDescription(promotionDTO.getDescription());
            promotion.setDiscountPercent(promotionDTO.getDiscountPercent());

            LOG.info("Saving Promotion");
            return promotionRepository.save(promotion);
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/promotion/all
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    // api/promotion/:productId/promotions
    public List<Promotion> productByIdPromotions(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));
        return product.getPromotions();
    }

    // api/promotion/:promotionId/:productId/add
    public Promotion addProductByIdInPromotion(Long promotionId, Long productId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Promotion promotion = promotionRepository.findById(promotionId)
                    .orElseThrow(() -> new PromotionNotFoundException("Promotion cannot be found"));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));

            promotion.getProducts().add(product);
            Promotion resultPromotion = promotionRepository.save(promotion);

            product.getPromotions().add(resultPromotion);
            productRepository.save(product);

            return resultPromotion;
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/promotion/:promotionId/:productId/delete
    public void deleteProductByIdFromPromotionById(Long promotionId, Long productId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Promotion promotion = promotionRepository.findById(promotionId)
                    .orElseThrow(() -> new PromotionNotFoundException("Promotion cannot be found"));

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));

            promotion.getProducts().remove(product);
            promotionRepository.save(promotion);

            product.getPromotions().remove(promotion);
            productRepository.save(product);
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/promotion/:promotionId/delete
    public void deletePromotionById(Long promotionId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Promotion promotion = promotionRepository.findById(promotionId)
                    .orElseThrow(() -> new PromotionNotFoundException("Promotion cannot be found"));
            List<Product> products = promotion.getProducts();
            for (Product product : products) {
                product.getPromotions().remove(promotion);
                productRepository.save(product);
            }
            promotion.getProducts().clear();

            promotionRepository.delete(promotion);
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    private Customer getCustomerByPrincipal(Principal principal) {
        String username = principal.getName();
        return customerRepository.findCustomerByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
    }
}
