package com.example.storeserver.services;

import com.example.storeserver.dto.ProductDTO;
import com.example.storeserver.entity.*;
import com.example.storeserver.entity.enums.EnumRole;
import com.example.storeserver.exceptions.*;
import com.example.storeserver.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    public static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CartRepository cartRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final OrderProductRepository orderProductRepository;
    private final PromotionRepository promotionRepository;
    private final SizeRepository sizeRepository;


    @Autowired
    public ProductService(CustomerRepository customerRepository, ProductRepository productRepository, BrandRepository brandRepository, CartRepository cartRepository, CategoryRepository categoryRepository, ColorRepository colorRepository, OrderProductRepository orderProductRepository, PromotionRepository promotionRepository, SizeRepository sizeRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.cartRepository = cartRepository;
        this.categoryRepository = categoryRepository;
        this.colorRepository = colorRepository;
        this.orderProductRepository = orderProductRepository;
        this.promotionRepository = promotionRepository;
        this.sizeRepository = sizeRepository;
    }

    // api/product/create
    public Product createProduct(ProductDTO productDTO, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Product product = new Product();
            product.setTitle(productDTO.getTitle());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setLikes(0);

            LOG.info("Saving Product");
            return productRepository.save(product);
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/product/all
    public List<Product> getAllProducts() {
        return productRepository.findAllByOrderByCreatedDateDesc();
    }

    // api/product/:productId
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));
    }

    // api/product/:productId/delete
    public void deleteProduct(Long productId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Product product = getProductById(productId);

            List<Cart> carts = product.getCarts();
            for (Cart cart : carts) {
                cart.getProducts().remove(product);
                Double totalPrice = cart.getTotalPrice();
                totalPrice -= Double.parseDouble(product.getPrice());
                cart.setTotalPrice(totalPrice);
                cartRepository.save(cart);
            }
            product.getCarts().clear();

            List<Color> colors = product.getColors();
            for (Color color : colors) {
                color.getProducts().remove(product);
                colorRepository.save(color);
            }
            product.getColors().clear();

            List<OrderProduct> orderProducts = product.getOrderProducts();
            for (OrderProduct orderProduct : orderProducts) {
                orderProduct.getProducts().remove(product);
                orderProductRepository.save(orderProduct);
            }
            product.getOrderProducts().clear();

            List<Promotion> promotions = product.getPromotions();
            for (Promotion promotion : promotions) {
                promotion.getProducts().remove(product);
                promotionRepository.save(promotion);
            }
            product.getPromotions().clear();

            List<Size> sizes = product.getSizes();
            for (Size size : sizes) {
                size.getProducts().remove(product);
                sizeRepository.save(size);
            }
            product.getSizes().clear();


            productRepository.delete(product);
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/product/update
    @Transactional
    public Product updateProduct(ProductDTO productDTO, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        if (customer.getRoles().contains(EnumRole.ROLE_ADMIN)) {
            Product product = getProductById(productDTO.getId());
            product.setTitle(productDTO.getTitle());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setLikes(0);
            product.setBrand(productDTO.getBrand());
            product.setCategory(productDTO.getCategory());

            // удаляем цвета текущего продукта
            for (Color color : product.getColors()) {
                color.getProducts().remove(product);
                colorRepository.save(color);
            }
            List<Color> colors = new ArrayList<>();
            productDTO.getColors().forEach(color -> colors.add(colorRepository.findById(color.getId()).get()));
            // обновляем цвета текущего продукта
            product.setColors(colors);
            for (Color color : colors) {
                color.getProducts().add(product);
                colorRepository.save(color);
            }

            // удаляем размеры текущего продукта
            for (Size size : product.getSizes()) {
                size.getProducts().remove(product);
                sizeRepository.save(size);
            }
            List<Size> sizes = new ArrayList<>();
            productDTO.getSizes().forEach(size -> sizes.add(sizeRepository.findById(size.getId()).get()));
            // обновляем размеры текущего продукта
            product.setSizes(sizes);
            for (Size size : sizes) {
                size.getProducts().add(product);
                sizeRepository.save(size);
            }

            // удаляем акции текущего продукта
            for (Promotion promotion : product.getPromotions()) {
                promotion.getProducts().remove(product);
                promotionRepository.save(promotion);
            }
            List<Promotion> promotions = new ArrayList<>();
            productDTO.getPromotions().forEach(promotion -> promotions.add(promotionRepository.findById(promotion.getId()).get()));
            // обновляем акции текущего продукта
            product.setPromotions(promotions);
            for (Promotion promotion : promotions) {
                promotion.getProducts().add(product);
                promotionRepository.save(promotion);
            }

            LOG.info("Saving Product");
            return productRepository.save(product);
        } else {
            LOG.info("You don't have enough authority");
            throw new ExceptionIsNotEnoughAuthority("You don't have enough authority");
        }
    }

    // api/product/brand/products
    public List<Product> getAllProductsByBrand(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException("Brand cannot be found"));
        return productRepository.findAllByBrandOrderByCreatedDateDesc(brand);
    }

    // api/product/category/products
    public List<Product> getAllProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CartNotFoundException("Category cannot be found"));
        return productRepository.findAllByCategoryOrderByCreatedDateDesc(category);
    }

    // api/product/customerCart/products
    public List<Product> getAllProductsByCustomerCart(Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        Cart cart = customer.getCart();
        return cart.getProducts();
    }

    // api/product/orderProductId/products
    public List<Product> getAllProductsForOrderProductById(Long orderProductId) {
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> new OrderProductNotFoundException("OrderProduct cannot be found"));
        return orderProduct.getProducts();
    }

    private Customer getCustomerByPrincipal(Principal principal) {
        String username = principal.getName();
        return customerRepository.findCustomerByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
    }
}
