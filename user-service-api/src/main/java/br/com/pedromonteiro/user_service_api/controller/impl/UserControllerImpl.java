package br.com.pedromonteiro.user_service_api.controller.impl;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedromonteiro.user_service_api.controller.UserController;
import br.com.pedromonteiro.user_service_api.service.UserService;
import lombok.RequiredArgsConstructor;
import models.requests.CreateUserRequest;
import models.responses.UserResponse;



@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController{

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponse> findById(final String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Override
    public ResponseEntity<Void> save(final CreateUserRequest createUserRequest) {
        userService.save(createUserRequest);
        return ResponseEntity.status(CREATED).build();
    }

    @Override
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }
    
}
