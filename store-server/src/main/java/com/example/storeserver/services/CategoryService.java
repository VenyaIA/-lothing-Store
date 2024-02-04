package com.example.storeserver.services;

import com.example.storeserver.dto.CategoryDTO;
import com.example.storeserver.entity.Category;
import com.example.storeserver.entity.Customer;
import com.example.storeserver.entity.Product;
import com.example.storeserver.entity.enums.EnumRole;
import com.example.storeserver.exceptions.CategoryNotFoundException;
import com.example.storeserver.exceptions.ExceptionIsNotEnoughAuthority;
import com.example.storeserver.exceptions.ProductNotFoundException;
import com.example.storeserver.repositories.CategoryRepository;
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
public class CategoryService {
    public static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    // api/category/create
    public Category createCategory(CategoryDTO categoryDTO, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Category category = new Category();
            category.setName(categoryDTO.getName());
            category.setDescription(categoryDTO.getDescription());

            LOG.info("Saving category");
            return categoryRepository.save(category);
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/category/all
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // api/category/:categoryId
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category cannot be found"));
    }

    // api/category/:categoryId/:productId/add
    public Category addProductByIdInCategory(Long categoryId, Long productId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Category category = getCategoryById(categoryId);
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));
            category.getProducts().add(product);
            Category resultCategory = categoryRepository.save(category);

            product.setCategory(resultCategory);
            productRepository.save(product);

            return resultCategory;
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/category/:categoryId/delete
    public void deleteCategory(Long categoryId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Category category = getCategoryById(categoryId);
            List<Product> products = category.getProducts();
            for (Product product : products) {
                product.setCategory(null);
                productRepository.save(product);
            }
            category.getProducts().clear();

            categoryRepository.delete(category);
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
