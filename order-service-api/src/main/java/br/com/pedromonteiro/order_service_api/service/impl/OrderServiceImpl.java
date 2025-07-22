package br.com.pedromonteiro.order_service_api.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.pedromonteiro.order_service_api.clients.UserServiceFeignClient;
import br.com.pedromonteiro.order_service_api.entity.Order;
import br.com.pedromonteiro.order_service_api.mapper.OrderMapper;
import br.com.pedromonteiro.order_service_api.repository.OrderRepository;
import br.com.pedromonteiro.order_service_api.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import models.dtos.OrderCreatedMessage;
import models.enums.OrderStatusEnum;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;
import models.responses.UserResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService{

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final UserServiceFeignClient userServiceFeignClient;
    private final RabbitTemplate rabbitTemplate;


    @Override
    public Order findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id + ", Type: " + Order.class.getSimpleName()));
    }
    
    @Override
    public void save(CreateOrderRequest request) {
        final var requester = validateUserId(request.requesterId());
        final var customer = validateUserId(request.customerId());
        final var entity = repository.save(mapper.fromRequest(request));

        log.info("Requester: {}", requester);
        log.info("Customer: {}", customer);

        

        rabbitTemplate.convertAndSend(
            "helpdesk",
            "rk.orders.create",
            new OrderCreatedMessage(mapper.fromEntity(entity), customer, requester)
        );
    }

    @Override
    public OrderResponse update(Long id, UpdateOrderRequest request) {
        validateUsers(request);

        Order entity = findById(id);

        entity =  mapper.fromRequest(entity, request);

        if (entity.getStatus().equals(OrderStatusEnum.CLOSED)) {
            entity.setClosedAt(LocalDateTime.now());
        }

        return mapper.fromEntity(repository.save(entity));
    }

    private void validateUsers(UpdateOrderRequest request) {
        if(request.requesterId() != null) validateUserId(request.requesterId());
        if(request.customerId() != null) validateUserId(request.customerId());
    }

    @Override
    public void deleteById(Long id) {
        repository.delete(findById(id));
    }

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Order> findAllPaginated(Integer page, Integer linesPerPage, String direction, String orderBy) {
        PageRequest pageRequest = PageRequest.of(
            page,
            linesPerPage,
            Direction.valueOf(direction),
            orderBy
        );

        return repository.findAll(pageRequest);
    }

    
    UserResponse validateUserId(final String userId) {
        return userServiceFeignClient.findById(userId).getBody();
    }
    

}
