package br.com.pedromonteiro.order_service_api.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.pedromonteiro.order_service_api.entity.Order;
import br.com.pedromonteiro.order_service_api.mapper.OrderMapper;
import br.com.pedromonteiro.order_service_api.repository.OrderRepository;
import br.com.pedromonteiro.order_service_api.service.OrderService;
import lombok.RequiredArgsConstructor;
import models.enums.OrderStatusEnum;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository repository;
    private final OrderMapper mapper;
    
    @Override
    public Order findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id + ", Type: " + Order.class.getSimpleName()));
    }
    
    @Override
    public void save(CreateOrderRequest createOrderRequest) {
        repository.save(mapper.fromRequest(createOrderRequest));
    }

    @Override
    public OrderResponse update(Long id, UpdateOrderRequest request) {
        Order entity = findById(id);

        entity =  mapper.fromRequest(entity, request);

        if (entity.getStatus().equals(OrderStatusEnum.CLOSED)) {
            entity.setClosedAt(LocalDateTime.now());
        }

        return mapper.fromEntity(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.delete(findById(id));
    }

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    
    
    

}
