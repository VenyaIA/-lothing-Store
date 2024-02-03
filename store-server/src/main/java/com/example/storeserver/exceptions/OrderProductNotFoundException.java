package com.example.storeserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderProductNotFoundException extends RuntimeException {
    public OrderProductNotFoundException(String msg) {
        super(msg);
    }
}
