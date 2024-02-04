package com.example.storeserver.services;

import com.example.storeserver.dto.PaymentDTO;
import com.example.storeserver.entity.OrderProduct;
import com.example.storeserver.entity.Payment;
import com.example.storeserver.entity.Product;
import com.example.storeserver.exceptions.OrderProductNotFoundException;
import com.example.storeserver.exceptions.PaymentNotFoundException;
import com.example.storeserver.repositories.OrderProductRepository;
import com.example.storeserver.repositories.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {
    public static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final OrderProductRepository orderProductRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, OrderProductRepository orderProductRepository) {
        this.paymentRepository = paymentRepository;
        this.orderProductRepository = orderProductRepository;
    }

    // api/payment/:orderProductId/create
    public Payment createPayment(PaymentDTO paymentDTO, Long orderProductId) {
        Payment payment = new Payment();
        OrderProduct orderProduct = orderProductRepository
                .findById(orderProductId).orElseThrow(() -> new OrderProductNotFoundException("OrderProduct cannot be found"));

        payment.setPaymentType(paymentDTO.getPaymentType());
        payment.setStatus(paymentDTO.getStatus());
        BigDecimal amount = BigDecimal.valueOf(0);
        for (Product product : orderProduct.getProducts()) {
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(product.getPrice()));
            LOG.info("Product Id: " + product.getId() + " price: " + price);
            amount = amount.add(price);
            LOG.info("Amount payment: " + amount);
        }
        payment.setAmount(amount);
        payment.setOrderProduct(orderProduct);

        Payment createdPayment = paymentRepository.save(payment);

        orderProduct.setPayment(createdPayment);
        orderProductRepository.save(orderProduct);

        LOG.info("Saving Payment");
        return createdPayment;
    }

    // api/payment/:orderProductId
    public Payment getPaymentByOrderProductId(Long orderProductId) {
        return paymentRepository.findByOrderProductId(orderProductId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment cannot be found"));
    }

    // api/payment/:orderProductId/update
    public Payment updateStatusByOrderProductId(Long orderProductId) {
        Payment payment = getPaymentByOrderProductId(orderProductId);
        OrderProduct orderProduct = orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> new OrderProductNotFoundException("Order cannot be found"));

        orderProduct.setStatus("Оплачен");
        orderProductRepository.save(orderProduct);

        payment.setStatus("Оплачен");
        return paymentRepository.save(payment);
    }

}
