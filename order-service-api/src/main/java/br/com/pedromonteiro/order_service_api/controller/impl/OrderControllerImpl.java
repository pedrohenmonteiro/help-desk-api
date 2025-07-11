package br.com.pedromonteiro.order_service_api.controller.impl;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedromonteiro.order_service_api.controller.OrderController;
import br.com.pedromonteiro.order_service_api.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import models.requests.CreateOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;

@RestController
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController{

    private final OrderService service;

    @Override
    public ResponseEntity<Void> save(@Valid CreateOrderRequest request) {
        service.save(request);
        return ResponseEntity.status(CREATED).build();
    }

    @Override
    public ResponseEntity<OrderResponse> update(Long id, @Valid UpdateOrderRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }
    
}
