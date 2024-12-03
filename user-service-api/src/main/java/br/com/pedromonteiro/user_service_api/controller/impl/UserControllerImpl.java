package br.com.pedromonteiro.user_service_api.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.pedromonteiro.user_service_api.controller.UserController;
import br.com.pedromonteiro.user_service_api.entity.User;
import br.com.pedromonteiro.user_service_api.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController{

    private final UserService userService;

    @Override
    public ResponseEntity<User> findById(String id) {
        return ResponseEntity.ok(userService.findById(id));
    }
    
}
