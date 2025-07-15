package br.com.pedromonteiro.order_service_api.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import models.responses.UserResponse;

@FeignClient(
    name = "user-service-api",
    url = "http://localhost:8080/api/users"

)
public interface UserServiceFeignClient {
    @GetMapping("/{id}")
    ResponseEntity<UserResponse> findById(@PathVariable("id") final String id);
}
