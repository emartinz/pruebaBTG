package com.btg.pruebaBTG.infrastructure.adapter.in;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btg.pruebaBTG.application.service.TransactionService;
import com.btg.pruebaBTG.domain.model.entities.Transaction;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Método que obtiene el historial de transacciones de un usuario.
     * @param userId Id del usuario
     * @return Lista de transacciones
     */
    @GetMapping("/get/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String userId) {
        List<Transaction> transactions = transactionService.getTransactionHistory(userId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * Método que obtiene transacciones de un usuario según su tipo.
     * @param userId Id del usuario
     * @param type Tipo de transacción (subscription o cancellation)
     * @return Lista de transacciones filtradas por tipo
     */
    @GetMapping("/get/{userId}/{type}")
    public ResponseEntity<List<Transaction>> getTransactionsByType(@PathVariable String userId, @PathVariable String type) {
        List<Transaction> transactions = transactionService.getTransactionsByType(userId, type);
        return ResponseEntity.ok(transactions);
    }
}
