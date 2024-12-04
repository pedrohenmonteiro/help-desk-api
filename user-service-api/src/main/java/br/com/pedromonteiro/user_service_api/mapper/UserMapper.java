package br.com.pedromonteiro.user_service_api.mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.pedromonteiro.user_service_api.entity.User;
import models.requests.CreateUserRequest;
import models.responses.UserResponse;

@Mapper(
    componentModel = "spring",
        nullValuePropertyMappingStrategy = IGNORE,
        nullValueCheckStrategy = ALWAYS
)
public interface UserMapper {
    
    UserResponse fromEntity(final User entity);

    @Mapping(target = "id", ignore = true)
    User fromRequest(CreateUserRequest createUserRequest);
}
