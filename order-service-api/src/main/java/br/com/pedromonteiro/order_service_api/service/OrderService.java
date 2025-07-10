package br.com.pedromonteiro.order_service_api.service;

import models.requests.CreateOrderRequest;

public interface OrderService {
    
    void save(CreateOrderRequest request);
}
