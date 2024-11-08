package com.btg.pruebaBTG.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.btg.pruebaBTG.domain.model.entities.Transaction;
import com.btg.pruebaBTG.infrastructure.adapter.out.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Método que obtiene el historial de transacciones de un usuario específico.
     * @param userId Id del usuario
     * @return Lista de transacciones
     */
    public List<Transaction> getTransactionHistory(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    /**
     * Método que obtiene transacciones de un usuario según su tipo (por ejemplo, "subscription" o "cancellation").
     * @param userId Id del usuario
     * @param type Tipo de transacción
     * @return Lista de transacciones filtradas por tipo
     */
    public List<Transaction> getTransactionsByType(String userId, String type) {
        return transactionRepository.findByUserIdAndType(userId, type);
    }
}