package br.com.pedromonteiro.user_service_api.service;

import org.springframework.stereotype.Service;

import br.com.pedromonteiro.user_service_api.mapper.UserMapper;
import br.com.pedromonteiro.user_service_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import models.exceptions.ResourceNotFoundException;
import models.responses.UserResponse;


@Service
@RequiredArgsConstructor
public class UserService {
 
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponse findById(final String id) {
        return userMapper.fromEntity(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Object not found. Id: " + id + ", Type: " + UserResponse.class.getSimpleName())));

    }
}
