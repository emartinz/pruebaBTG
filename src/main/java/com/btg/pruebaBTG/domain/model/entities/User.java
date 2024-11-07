package com.btg.pruebaBTG.domain.model.entities;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String name;
    private double balance;
    private String preferredNotification;
    private String email;
    private String phoneNumber;
}