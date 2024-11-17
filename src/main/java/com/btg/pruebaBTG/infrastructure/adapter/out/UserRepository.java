package com.btg.pruebaBTG.infrastructure.adapter.out;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.btg.pruebaBTG.domain.model.entities.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findById(String id);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
