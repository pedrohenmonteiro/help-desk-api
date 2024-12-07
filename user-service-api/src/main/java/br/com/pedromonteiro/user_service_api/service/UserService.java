package br.com.pedromonteiro.user_service_api.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.pedromonteiro.user_service_api.mapper.UserMapper;
import br.com.pedromonteiro.user_service_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateUserRequest;
import models.responses.UserResponse;


@Service
@RequiredArgsConstructor
public class UserService {
 
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponse findById(final String id) {
        return userMapper.fromEntity(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Object not found. Id: " + id + ", Type: " + UserResponse.class.getSimpleName())));

    }

    public void save(CreateUserRequest createUserRequest) {
        validateEmail(createUserRequest.email(), null);
        var user = userMapper.fromRequest(createUserRequest);
        userRepository.save(user);
    }

    private void validateEmail(final String email, final String id) {
       userRepository.findByEmail(email)
            .filter(user -> !user.getId().equals(id))
            .ifPresent(user -> {
                throw new DataIntegrityViolationException("Email " + "'" + email + "' already exists");
            });
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
            .stream()
            .map(userMapper::fromEntity)
            .toList();
    }
}
