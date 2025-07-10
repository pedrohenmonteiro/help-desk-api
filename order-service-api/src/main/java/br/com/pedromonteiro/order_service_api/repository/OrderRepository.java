package br.com.pedromonteiro.order_service_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pedromonteiro.order_service_api.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
