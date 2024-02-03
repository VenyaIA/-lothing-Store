package com.example.storeserver.services;

import com.example.storeserver.dto.SizeDTO;
import com.example.storeserver.entity.Color;
import com.example.storeserver.entity.Customer;
import com.example.storeserver.entity.Product;
import com.example.storeserver.entity.Size;
import com.example.storeserver.entity.enums.EnumRole;
import com.example.storeserver.exceptions.ExceptionIsNotEnoughAuthority;
import com.example.storeserver.exceptions.ProductNotFoundException;
import com.example.storeserver.exceptions.SizeNotFoundException;
import com.example.storeserver.repositories.CustomerRepository;
import com.example.storeserver.repositories.ProductRepository;
import com.example.storeserver.repositories.SizeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

/**
 * api/size/create – POST create new Size
 * api/size/:sizeId/delete – POST delete Size
 * api/size/:productId/all – all product size
 */

@Service
public class SizeService {
    public static final Logger LOG = LoggerFactory.getLogger(SizeService.class);

    private final SizeRepository sizeRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;


    @Autowired
    public SizeService(SizeRepository sizeRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.sizeRepository = sizeRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    // api/size/create
    public Size createSize(SizeDTO sizeDTO, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Size size = new Size();
            size.setTitle(sizeDTO.getTitle());

            LOG.info("Saving Size");
            return sizeRepository.save(size);
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/size/all
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }

    // api/size/:productId/sizes
    public List<Size> productByIdSizes(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));
        return product.getSizes();
    }

    // api/size/:sizeId/:productId/add
    public Size addProductByIdInSize(Long sizeId, Long productId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Size size = sizeRepository.findById(sizeId)
                    .orElseThrow(() -> new SizeNotFoundException("Size cannot be found"));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));

            size.getProducts().add(product);
            Size resultSize = sizeRepository.save(size);

            product.getSizes().add(size);
            productRepository.save(product);

            return resultSize;
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/size/:sizeId/:productId/delete
    public void deleteProductByIdFromSizeById(Long sizeId, Long productId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Size size = sizeRepository.findById(sizeId)
                    .orElseThrow(() -> new SizeNotFoundException("Size cannot be found"));

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));

            size.getProducts().remove(product);
            sizeRepository.save(size);

            product.getSizes().remove(size);
            productRepository.save(product);
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/size/:sizeId/delete
    public void deleteSizeById(Long sizeId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Size size = sizeRepository.findById(sizeId)
                    .orElseThrow(() -> new SizeNotFoundException("Size cannot be found"));
            List<Product> products = size.getProducts();
            for (Product product : products) {
                product.getSizes().remove(size);
                productRepository.save(product);
            }
            size.getProducts().clear();

            sizeRepository.delete(size);
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
