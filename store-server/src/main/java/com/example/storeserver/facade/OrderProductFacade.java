package com.example.storeserver.facade;

import com.example.storeserver.dto.OrderProductDTO;
import com.example.storeserver.entity.OrderProduct;
import org.springframework.stereotype.Component;

@Component
public class OrderProductFacade {

    public OrderProductDTO orderProductToOrderProductDTO(OrderProduct orderProduct) {
        OrderProductDTO orderProductDTO = new OrderProductDTO();
        orderProductDTO.setId(orderProduct.getId());
        orderProductDTO.setStatus(orderProduct.getStatus());

        return orderProductDTO;
    }

}
