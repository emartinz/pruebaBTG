package com.btg.pruebaBTG.infrastructure.adapter.out.interfaces;

import java.util.Optional;

import com.btg.pruebaBTG.domain.model.entities.User;

public interface IUserRepository {
    Optional<User> findById(String id);
    User save(User user);
}
