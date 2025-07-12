package br.com.pedromonteiro.order_service_api.controller.impl;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedromonteiro.order_service_api.controller.OrderController;
import br.com.pedromonteiro.order_service_api.mapper.OrderMapper;
import br.com.pedromonteiro.order_service_api.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import models.requests.CreateOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;

@RestController
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController{

    private final OrderService service;

    private final OrderMapper mapper;

    @Override
    public ResponseEntity<Void> save(@Valid CreateOrderRequest request) {
        service.save(request);
        return ResponseEntity.status(CREATED).build();
    }

    @Override
    public ResponseEntity<OrderResponse> update(Long id, @Valid UpdateOrderRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Override
    public ResponseEntity<OrderResponse> findById(@NotNull(message = "The order id must be informed") Long id) {
    
        return ResponseEntity.ok(
            mapper.fromEntity(service.findById(id))
        );
    }

    @Override
    public ResponseEntity<List<OrderResponse>> findAll() {
        return ResponseEntity.ok(
            mapper.fromEntities(service.findAll())
        );
    }

    @Override
    public ResponseEntity<Void> deleteById(@NotNull(message = "The order id must be informed") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Page<OrderResponse>> findAllPaginated(Integer page, Integer linesPerPage, String direction,
            String orderBy) {
        return ResponseEntity.ok(
            service.findAllPaginated(page, linesPerPage, direction, orderBy).map(
                mapper::fromEntity
            )
        );
    }
    
}
