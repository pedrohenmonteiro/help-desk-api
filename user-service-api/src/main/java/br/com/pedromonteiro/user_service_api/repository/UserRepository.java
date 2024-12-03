package br.com.pedromonteiro.user_service_api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.pedromonteiro.user_service_api.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
    
}
