package com.example.storeserver.services;

import com.example.storeserver.dto.BrandDTO;
import com.example.storeserver.entity.Brand;
import com.example.storeserver.entity.Customer;
import com.example.storeserver.entity.Product;
import com.example.storeserver.entity.enums.EnumRole;
import com.example.storeserver.exceptions.BrandNotFoundException;
import com.example.storeserver.exceptions.ExceptionIsNotEnoughAuthority;
import com.example.storeserver.exceptions.ProductNotFoundException;
import com.example.storeserver.repositories.BrandRepository;
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
public class BrandService {
    public static final Logger LOG = LoggerFactory.getLogger(BrandService.class);

    private final BrandRepository brandRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.brandRepository = brandRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    // api/brand/create
    public Brand createBrand(BrandDTO brandDTO, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Brand brand = new Brand();
            brand.setTitle(brandDTO.getTitle());
            brand.setDescription(brandDTO.getDescription());

            LOG.info("Saving brand");
            return brandRepository.save(brand);
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/brand/all
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    // api/brand/:brandId
    public Brand getBrandById(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(()-> new BrandNotFoundException("Brand cannot be found"));
    }

    // api/brand/:brandId/:productId/add
    public Brand addProductByIdInBrand(Long brandId, Long productId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Brand brand = getBrandById(brandId);
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));
            brand.getProducts().add(product);
            Brand resultBrand = brandRepository.save(brand);

            product.setBrand(resultBrand);
            productRepository.save(product);

            return resultBrand;
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/brand/:brandId/delete
    public void deleteBrandById(Long brandId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Brand brand = getBrandById(brandId);
            List<Product> products = brand.getProducts();
            for (Product product : products) {
                product.setBrand(null);
                productRepository.save(product);
            }
            brand.getProducts().clear();

            brandRepository.delete(brand);
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
