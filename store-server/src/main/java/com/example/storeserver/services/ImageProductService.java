package com.example.storeserver.services;

import com.example.storeserver.dto.ImageProductDTO;
import com.example.storeserver.entity.Customer;
import com.example.storeserver.entity.ImageProduct;
import com.example.storeserver.entity.Product;
import com.example.storeserver.entity.enums.EnumRole;
import com.example.storeserver.exceptions.ExceptionIsNotEnoughAuthority;
import com.example.storeserver.exceptions.ImageProductNotFoundException;
import com.example.storeserver.exceptions.ProductNotFoundException;
import com.example.storeserver.repositories.CustomerRepository;
import com.example.storeserver.repositories.ImageProductRepository;
import com.example.storeserver.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
public class ImageProductService {
    public static final Logger LOG = LoggerFactory.getLogger(ImageProductService.class);

    private final ImageProductRepository imageProductRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ImageProductService(ImageProductRepository imageProductRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.imageProductRepository = imageProductRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    // api/imageProduct/:productId/upload
    public ImageProduct uploadImageProductById(ImageProductDTO imageProductDTO, Long productId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            ImageProduct imageProduct = new ImageProduct();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));
            imageProduct.setUrl(imageProductDTO.getUrl());
            imageProduct.setProduct(product);

            LOG.info("Saving color");
            return imageProductRepository.save(imageProduct);
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/imageProduct/:productId/images
    public List<ImageProduct> getAllImagesProductById(Long productId) {
        return imageProductRepository.findAllByProductId(productId);
    }

    // api/imageProduct/:productId/:imageId
    public ImageProduct getImageProductByIdInProductById(Long productId, Long imageId) {
        return imageProductRepository.findByIdAndProductId(imageId, productId)
                .orElseThrow(() -> new ImageProductNotFoundException("ImageProduct cannot be found"));
    }

    // api/imageProduct/:productId/:imageId/delete
    @Transactional
    public void deleteImageProductByIdInProductById(Long productId, Long imageId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            imageProductRepository.deleteByIdAndProductId(imageId, productId);
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
