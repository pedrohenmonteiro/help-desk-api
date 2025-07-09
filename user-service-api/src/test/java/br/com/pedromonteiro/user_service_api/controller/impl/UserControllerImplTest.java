package br.com.pedromonteiro.user_service_api.controller.impl;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pedromonteiro.user_service_api.entity.User;
import br.com.pedromonteiro.user_service_api.repository.UserRepository;
import models.enums.ProfileEnum;
import models.requests.CreateUserRequest;
import models.responses.UserResponse;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerImplTest {

        private static final String BASE_URI = "/api/users";
        static final User DEFAULT_USER = new User("1", "Foo", "foo@gmail.com", "password", new HashSet<>(List.of(ProfileEnum.ROLE_ADMIN)));
        static final User DEFAULT_USER2 = new User("2", "Bar", "bar@gmail.com", "password", new HashSet<>(List.of(ProfileEnum.ROLE_ADMIN)));
        static final CreateUserRequest DEFAULT_CREATE_USER_REQUEST = new CreateUserRequest("Foo", "foo@gmail.com", "password", new HashSet<>(List.of(ProfileEnum.ROLE_ADMIN)));



    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByIdWithSuccess() throws Exception {

        final var entity = userRepository.save(DEFAULT_USER);

        mockMvc.perform(get(BASE_URI + "/{id}", entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(entity.getId()))
                .andExpect(jsonPath("$.name").value(entity.getName()))
                .andExpect(jsonPath("$.email").value(entity.getEmail()))
                .andExpect(jsonPath("$.profiles").isArray());

        userRepository.delete(entity);
    }

    @Test
    void testFindByIdWithNotFoundException() throws Exception {
        final String id = "non-existing-id";
        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Object not found. Id: " + id + ", Type: " + UserResponse.class.getSimpleName()))
                .andExpect(jsonPath("$.status").value(NOT_FOUND.value()))
                .andExpect(jsonPath("$.path").value("/api/users/" + id))
                .andExpect(jsonPath("$.error").value(NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    void testFindAllWithSuccess() throws Exception {

        userRepository.saveAll(List.of(DEFAULT_USER, DEFAULT_USER2));

        mockMvc.perform(get(BASE_URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").isNotEmpty())
                .andExpect(jsonPath("$[1]").isNotEmpty())
                .andExpect(jsonPath("$[0].profiles").isArray());

        userRepository.deleteAll(List.of(DEFAULT_USER, DEFAULT_USER2));
    }

    @Test
    void testSaveUserWithSuccess() throws Exception {

        mockMvc.perform(post(BASE_URI)
            .contentType(APPLICATION_JSON)
            .content(toJson(DEFAULT_CREATE_USER_REQUEST))
            ).andExpect(status().isCreated());

            userRepository.deleteByEmail(DEFAULT_CREATE_USER_REQUEST.email());
    }

        @Test
    void testSaveUserWithConflict() throws Exception {
        userRepository.save(DEFAULT_USER);
        
        mockMvc.perform(post(BASE_URI)
        .contentType(APPLICATION_JSON)
        .content(toJson(DEFAULT_CREATE_USER_REQUEST))
        ).andExpect(status().isConflict())
         .andExpect(jsonPath("$.message").value("Email '" + DEFAULT_USER.getEmail() + "' already exists"))
         .andExpect(jsonPath("$.status").value(CONFLICT.value()))
         .andExpect(jsonPath("$.timestamp").isNotEmpty())
         .andExpect(jsonPath("$.error").value(CONFLICT.getReasonPhrase()))
         .andExpect(jsonPath("$.path").value(BASE_URI));

        
        userRepository.delete(DEFAULT_USER);
    }

    @Test
    void testSaveUserWithNameEmptyThenThrowsBadRequest() throws Exception {
        var request = DEFAULT_CREATE_USER_REQUEST.withName("");

        mockMvc.perform(post(BASE_URI)
        .contentType(APPLICATION_JSON)
        .content(toJson(request))
        ).andExpect(status().isBadRequest())
         .andExpect(jsonPath("$.message").value("Exception in validation attributes"))
         .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
         .andExpect(jsonPath("$.timestamp").isNotEmpty())
         .andExpect(jsonPath("$.error").value("Validation Exception"))
         .andExpect(jsonPath("$.path").value(BASE_URI))
         .andExpect(jsonPath("$.errors[?(@.fieldName == 'name' && @.message == 'Name must be between 3 and 50 characters')]").exists())
         .andExpect(jsonPath("$.errors[?(@.fieldName == 'name' && @.message == 'Name cannot be empty')]").exists());
    }

    @Test
    void testUpdate() {

    }

    private String toJson(final Object obj) throws Exception {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new Exception("Error converting object to JSON", e);
        }
    }
}
