package com.example.storeserver.services;

import com.example.storeserver.dto.OrderProductDTO;
import com.example.storeserver.entity.Customer;
import com.example.storeserver.entity.OrderProduct;
import com.example.storeserver.entity.Payment;
import com.example.storeserver.entity.Product;
import com.example.storeserver.exceptions.ProductNotFoundException;
import com.example.storeserver.repositories.CustomerRepository;
import com.example.storeserver.repositories.OrderProductRepository;
import com.example.storeserver.repositories.PaymentRepository;
import com.example.storeserver.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderProductService {
    public static final Logger LOG = LoggerFactory.getLogger(OrderProductService.class);

    private final OrderProductRepository orderProductRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public OrderProductService(OrderProductRepository orderProductRepository, CustomerRepository customerRepository, ProductRepository productRepository, PaymentRepository paymentRepository) {
        this.orderProductRepository = orderProductRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.paymentRepository = paymentRepository;
    }

    // api/order/create
    public OrderProduct createOrderProduct(OrderProductDTO orderProductDTO, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);

        OrderProduct orderProduct = new OrderProduct();

        orderProduct.setStatus(orderProductDTO.getStatus());
        orderProduct.setCustomer(customer);

        LOG.info("Saving OrderProduct");
        return orderProductRepository.save(orderProduct);
    }

    // api/order/customer/:orderId
    public OrderProduct getOrderProductByIdForCustomer(Long orderId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        List<OrderProduct> orderProducts = orderProductRepository.findAllByCustomer(customer);
        OrderProduct orderProduct = orderProducts.stream()
                .filter(orderProduct1 -> orderProduct1.getId() == orderId).collect(Collectors.toList()).get(0);

        return orderProduct;
    }

    // api/order/customer/:orderId/:productId/add
    public OrderProduct addProductByIdInOrderProductById(Long orderId, Long productId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        List<OrderProduct> orderProducts = orderProductRepository.findAllByCustomer(customer);
        OrderProduct orderProduct = orderProducts.stream()
                .filter(orderProduct1 -> orderProduct1.getId() == orderId).collect(Collectors.toList()).get(0);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));
        orderProduct.getProducts().add(product);
        OrderProduct resultOrderProduct = orderProductRepository.save(orderProduct);

        product.getOrderProducts().add(resultOrderProduct);
        productRepository.save(product);

        return resultOrderProduct;
    }


    // api/order/customer/all
    public List<OrderProduct> getAllOrderProductForCustomer(Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        List<OrderProduct> orderProducts = orderProductRepository.findAllByCustomer(customer);

        return orderProducts;
    }

    // api/order/customer/:orderId/:productId/delete
    public void deleteProductByIdFromOrderProductById(Long orderId, Long productId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        List<OrderProduct> orderProducts = orderProductRepository.findAllByCustomer(customer);
        OrderProduct orderProduct = orderProducts.stream()
                .filter(orderProduct1 -> orderProduct1.getId() == orderId).collect(Collectors.toList()).get(0);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product cannot be found"));

        orderProduct.getProducts().remove(product);
        orderProductRepository.save(orderProduct);

        product.getOrderProducts().remove(orderProduct);
        productRepository.save(product);
    }

    // api/order/customer/:orderId/delete
    public void deleteOrderProductById(Long orderId, Principal principal) {
        Customer customer = getCustomerByPrincipal(principal);
        List<OrderProduct> orderProducts = orderProductRepository.findAllByCustomer(customer);
        OrderProduct orderProduct = orderProducts.stream()
                .filter(orderProduct1 -> orderProduct1.getId() == orderId).collect(Collectors.toList()).get(0);

        List<Product> products = orderProduct.getProducts();
        for (Product product : products) {
            product.getOrderProducts().remove(orderProduct);
            productRepository.save(product);
        }
        orderProduct.getProducts().clear();

        Payment payment = orderProduct.getPayment();
        if (payment != null) {
            payment.setOrderProduct(null);
            orderProduct.setPayment(null);
            paymentRepository.delete(payment);
        }


        orderProductRepository.delete(orderProduct);
    }


    private Customer getCustomerByPrincipal(Principal principal) {
        String username = principal.getName();
        return customerRepository.findCustomerByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
    }

}
