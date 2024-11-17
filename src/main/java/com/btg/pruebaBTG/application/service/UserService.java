package com.btg.pruebaBTG.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.btg.pruebaBTG.domain.model.entities.User;
import com.btg.pruebaBTG.infrastructure.adapter.out.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("El usuario con id: " + userId + " no fue encontrado."));
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El usuario con email: " + user.getEmail() + " ya existe");
        }
        return userRepository.save(user); // MongoDB generará automáticamente el ID
    }

    public User updateUser(String userId, User user) {
        // Verifica si el usuario existe
        Optional<User> existingUser = userRepository.findById(userId);
        if (!existingUser.isPresent()) {
            throw new RuntimeException("User with ID " + userId + " not found");
        }

        // Actualiza los valores necesarios
        User updatedUser = existingUser.get();
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setBalance(user.getBalance());
        // Guarda el usuario actualizado
        return userRepository.save(updatedUser);
    }

    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("El usuario con id: " + userId + " no fue encontrado.");
        }
        userRepository.deleteById(userId);
    }

    public double getBalance(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("El usuario con id: " + userId + " no fue encontrado."));
        return user.getBalance();
    }
}
