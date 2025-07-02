package br.com.pedromonteiro.user_service_api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.ALL_VALUE;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.pedromonteiro.user_service_api.entity.User;
import br.com.pedromonteiro.user_service_api.mapper.UserMapper;
import br.com.pedromonteiro.user_service_api.repository.UserRepository;
import models.enums.ProfileEnum;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateUserRequest;
import models.requests.UpdateUserRequest;
import models.responses.UserResponse;

@SpringBootTest
class UserServiceTest {

    static final UserResponse DEFAULT_USER_RESPONSE = new UserResponse("1", "Foo", "foo@gmail.com", "password", new HashSet<>(List.of(ProfileEnum.ROLE_ADMIN)));
    static final CreateUserRequest DEFAULT_CREATE_USER_REQUEST = new CreateUserRequest("Foo", "foo@gmail.com", "password", new HashSet<>(List.of(ProfileEnum.ROLE_ADMIN)));
    static final UpdateUserRequest DEFAULT_UPDATE_USER_REQUEST = new UpdateUserRequest("Bar", "foo@gmail.com", "password1", null);
    static final UpdateUserRequest DEFAULT_UPDATE_USER_REQUEST_EMAIL_ALREADY_EXISTS = new UpdateUserRequest("Bar", "foo@gmail.com", "password1", null);
    static final User DEFAULT_USER = new User("1", "Foo", "foo@gmail.com", "password", new HashSet<>(List.of(ProfileEnum.ROLE_ADMIN)));
    static final User DEFAULT_USER_UPDATED = new User("1", "Bar", "bar@gmail.com", "password1", new HashSet<>(List.of(ProfileEnum.ROLE_ADMIN)));



    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Test
    void whenCallFindAllThenReturnListOfUserResponse() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));
        when(userMapper.fromEntity(any(User.class))).thenReturn(DEFAULT_USER_RESPONSE);


        final var response = userService.findAll();
        assertThat(response).isNotNull().hasSize(2);
        assertThat(response.get(0).getClass()).isEqualTo(UserResponse.class);

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(2)).fromEntity(any(User.class));
    }

    @Test
    void whenCallFindByIdWithValidIdThenReturnUserResponse() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(new User()));
        when(userMapper.fromEntity(any(User.class))).thenReturn(DEFAULT_USER_RESPONSE);

        final var response = userService.findById("1");

        assertThat(response).isNotNull();
        assertThat(response.getClass()).isEqualTo(UserResponse.class);

        verify(userRepository, times(1)).findById(anyString());
        verify(userMapper, times(1)).fromEntity(any(User.class));
    }

    @Test
    void whenCallFindByIdWithInvalidIdThenThrowResourceNotFoundException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        try {
            userService.findById("1");
        } catch(Exception e) {
            assertThat(ResourceNotFoundException.class).isEqualTo(e.getClass());
        }

        verify(userRepository, times(1)).findById(anyString());
        verify(userMapper, times(0)).fromEntity(any(User.class));
    }

    @Test
    void whenCallSaveThenSuccess() {
        when(userMapper.fromRequest(any())).thenReturn(new User());
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(encoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        userService.save(DEFAULT_CREATE_USER_REQUEST);

        verify(userMapper).fromRequest(DEFAULT_CREATE_USER_REQUEST);
        verify(encoder).encode(DEFAULT_CREATE_USER_REQUEST.password());
        verify(userRepository).save(any(User.class));
        verify(userRepository).findByEmail(DEFAULT_CREATE_USER_REQUEST.email());
    }

      @Test
    void whenCallSaveWithInvalidEmailThenThrowDataIntegrityViolationException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(DEFAULT_USER));
        
        try {
            userService.save(DEFAULT_CREATE_USER_REQUEST);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(DataIntegrityViolationException.class);
            assertThat("Email " + "'" + DEFAULT_CREATE_USER_REQUEST.email() + "' already exists").isEqualTo(e.getMessage());
        }

        verify(userRepository, times(1)).findByEmail(DEFAULT_CREATE_USER_REQUEST.email());
        verify(userMapper, times(0)).fromRequest(any(CreateUserRequest.class));
        verify(userRepository, times(0)).save(any(User.class));
        verify(encoder, times(0)).encode(anyString());
    }

    @Test
    void whenCallUpdateWithInvalidIdThenThrowResourceNotFoundException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        try {
            userService.update("1", DEFAULT_UPDATE_USER_REQUEST);
        } catch(Exception e) {
            assertThat(e.getClass()).isEqualTo(ResourceNotFoundException.class);
        }

        verify(userRepository, times(1)).findById(anyString());
        verify(userMapper, times(0)).update(any(), any());
        verify(encoder, times(0)).encode(anyString());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void whenCallUpdateWithValidParamsThenSuccess() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(DEFAULT_USER));
        when(userRepository.findById(anyString())).thenReturn(Optional.of(DEFAULT_USER));
        when(userRepository.save(any(User.class))).thenReturn(DEFAULT_USER_UPDATED);
        when(userMapper.update(any(), any())).thenReturn(DEFAULT_USER_UPDATED);
        when(encoder.encode(anyString())).thenReturn("encoded");

        userService.update(DEFAULT_USER.getId(), DEFAULT_UPDATE_USER_REQUEST);

        verify(userRepository).findById(anyString());
        verify(userMapper).fromEntity(DEFAULT_USER_UPDATED);
        verify(encoder).encode(DEFAULT_UPDATE_USER_REQUEST.password());
        verify(userRepository).save(any(User.class));
        verify(userRepository).findByEmail(DEFAULT_UPDATE_USER_REQUEST.email());
    }

    @Test
    void whenCallUpdateWithInvalidEmailThenThrowDataIntegrityViolationException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(DEFAULT_USER));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(DEFAULT_USER));

        try {
            userService.update("100", DEFAULT_UPDATE_USER_REQUEST_EMAIL_ALREADY_EXISTS);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(DataIntegrityViolationException.class);
            assertThat("Email " + "'" + DEFAULT_UPDATE_USER_REQUEST_EMAIL_ALREADY_EXISTS.email() + "' already exists").isEqualTo(e.getMessage());
        }

        verify(userRepository, times(1)).findByEmail(DEFAULT_UPDATE_USER_REQUEST_EMAIL_ALREADY_EXISTS.email());
        verify(userRepository, times(1)).findById(anyString());
        verify(userMapper, times(0)).update(any(), any());
        verify(encoder, times(0)).encode(anyString());
        verify(userRepository, times(0)).save(any(User.class));
    }
}
