package com.btg.pruebaBTG.domain.model.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    private String id;
    private String userId;
    private String fundId;
    private String investmentId;
    private String type;    // Tipo de transaccion
    private double amount;
    private LocalDateTime date;
}
