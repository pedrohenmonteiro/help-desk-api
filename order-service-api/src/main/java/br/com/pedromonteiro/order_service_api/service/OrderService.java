package br.com.pedromonteiro.order_service_api.service;

import br.com.pedromonteiro.order_service_api.entity.Order;
import models.requests.CreateOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;

public interface OrderService {

    Order findById(final Long id);
    
    void save(CreateOrderRequest request);

    OrderResponse update(Long id, UpdateOrderRequest request);
}
