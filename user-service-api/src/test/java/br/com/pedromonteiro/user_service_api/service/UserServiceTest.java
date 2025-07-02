package br.com.pedromonteiro.user_service_api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.pedromonteiro.user_service_api.entity.User;
import br.com.pedromonteiro.user_service_api.mapper.UserMapper;
import br.com.pedromonteiro.user_service_api.repository.UserRepository;
import models.responses.UserResponse;

@SpringBootTest
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Test
    void testFindAll() {

    }

    @Test
    void whenCallFindByIdWithValidIdThenReturnUserResponse() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(new User()));
        when(userMapper.fromEntity(any(User.class))).thenReturn(mock(UserResponse.class));

        final var response = userService.findById("1");

        assertThat(response).isNotNull();
        assertThat(response.getClass()).isEqualTo(UserResponse.class);

        verify(userRepository, times(1)).findById(anyString());
        verify(userMapper, times(1)).fromEntity(any(User.class));
    }

    @Test
    void testFindUser() {

    }

    @Test
    void testSave() {

    }

    @Test
    void testUpdate() {

    }
}
