package br.com.pedromonteiro.order_service_api.service.impl;

import org.springframework.stereotype.Service;

import br.com.pedromonteiro.order_service_api.mapper.OrderMapper;
import br.com.pedromonteiro.order_service_api.repository.OrderRepository;
import br.com.pedromonteiro.order_service_api.service.OrderService;
import lombok.RequiredArgsConstructor;
import models.requests.CreateOrderRequest;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository repository;
    private final OrderMapper mapper;
    
    @Override
    public void save(CreateOrderRequest createOrderRequest) {
        repository.save(mapper.fromRequest(createOrderRequest));
    }
    
}
