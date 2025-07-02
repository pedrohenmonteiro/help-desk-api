package br.com.pedromonteiro.user_service_api.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.pedromonteiro.user_service_api.entity.User;
import br.com.pedromonteiro.user_service_api.mapper.UserMapper;
import br.com.pedromonteiro.user_service_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import models.responses.UserResponse;


@Service
@RequiredArgsConstructor
public class UserService {
 
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder encoder;

    public UserResponse findById(final String id) {
        return userMapper.fromEntity(findUser(id));

    }

    public void save(CreateUserRequest request) {
        validateIfEmailAlreadyExists(request.email(), null);
        var user = userMapper.fromRequest(request)
                            .withPassword(encoder.encode(request.password()));
        userRepository.save(user);
    }

    private void validateIfEmailAlreadyExists(final String email, final String id) {
        // Se findByEmail vier empty, ignora filter e ifPresent
       userRepository.findByEmail(email)
            .filter(user -> !user.getId().equals(id))
            .ifPresent(user -> {
                throw new DataIntegrityViolationException("Email " + "'" + user.getEmail() + "' already exists");
            });
    }

    public User findUser(final String id) {
        return userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Object not found. Id: " + id + ", Type: " + UserResponse.class.getSimpleName()
                    ));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll()
            .stream()
            .map(userMapper::fromEntity)
            .toList();
    }

    public UserResponse update(final String id, final UpdateUserRequest request) {
        var entity = findUser(id);
        validateIfEmailAlreadyExists(request.email(), id);
        final var newEntity = userRepository.save(
            userMapper.update(request, entity)
                .withPassword(request.password() != null ? encoder.encode(request.password()) : entity.getPassword())
        );
        return userMapper.fromEntity(newEntity);
    }
}
 