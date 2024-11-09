package com.btg.pruebaBTG.infrastructure.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.btg.pruebaBTG.domain.model.entities.Fund;
import com.btg.pruebaBTG.domain.model.entities.User;
import com.btg.pruebaBTG.domain.model.enums.PreferredNotificationType;
import com.btg.pruebaBTG.infrastructure.adapter.out.FundRepository;
import com.btg.pruebaBTG.infrastructure.adapter.out.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Configuration
@Data
@AllArgsConstructor
public class DataInitializer {

    private final FundRepository fundRepository;
    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner initializeCategories() {
        return args -> {
            loadDefaultUsers();
            loadDefaultFunds();
        };
    }

    private void loadDefaultUsers() {
        //Lista de Usuarios predeterminados
        List<User> defaultUsers = Arrays.asList(
            new User(
                null, 
                "Andres Martinez", 
                500000, 
                PreferredNotificationType.EMAIL, 
                "correo@dominio.com", 
                "+573012345678"
            )
        );

        // Crea los Usuarios si no existen
        for (User user : defaultUsers) {
            if (!userRepository.existsByName(user.getName())) {
                userRepository.save(user);
            }
        }
    }

    private void loadDefaultFunds() {
        // Lista de Fondos predeterminados
        List<Fund> defaultFunds = Arrays.asList(
            new Fund(null, "FPV_BTG_PACTUAL_RECAUDADORA", 75000, "FPV"),
            new Fund(null, "FPV_BTG_PACTUAL_ECOPETROL", 125000, "FPV"),
            new Fund(null, "DEUDAPRIVADA", 50000, "FIC"),
            new Fund(null, "FDO-ACCIONES", 250000, "FIC"),
            new Fund(null, "FPV_BTG_PACTUAL_DINAMICA", 100000, "FPV")
        );

        // Crea los Fondos si no existen
        for (Fund fund : defaultFunds) {
            if (!fundRepository.existsByName(fund.getName())) {
                fundRepository.save(fund);
            }
        }
    }
}
