package br.com.pedromonteiro.user_service_api.service;

import org.springframework.stereotype.Service;

import br.com.pedromonteiro.user_service_api.entity.User;
import br.com.pedromonteiro.user_service_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
 
    private final UserRepository userRepository;

    public User findById(final String id) {
        return userRepository.findById(id).orElse(null);
    }
}
