package br.com.pedromonteiro.order_service_api.mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import java.time.LocalDateTime;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import br.com.pedromonteiro.order_service_api.entity.Order;
import models.enums.OrderStatusEnum;
import models.requests.CreateOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE,
        nullValueCheckStrategy = ALWAYS
)
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
    @Mapping(target = "createdAt", expression = "java(mapCreatedAt())")
    Order fromRequest(CreateOrderRequest request);

    OrderResponse fromEntity(Order save);

    @Named("mapStatus")
    default OrderStatusEnum mapStatus(final String status) {
        return OrderStatusEnum.toEnum(status);
    }

    default LocalDateTime mapCreatedAt() {
        return LocalDateTime.now();
    }

    List<OrderResponse> fromEntities(List<Order> orders);
}