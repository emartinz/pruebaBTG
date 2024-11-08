package com.btg.pruebaBTG;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.btg.pruebaBTG.application.service.TransactionService;
import com.btg.pruebaBTG.domain.model.entities.Fund;
import com.btg.pruebaBTG.domain.model.entities.Transaction;
import com.btg.pruebaBTG.domain.model.entities.User;
import com.btg.pruebaBTG.generic.GenericTest;
import com.btg.pruebaBTG.infrastructure.adapter.out.TransactionRepository;

public class TransactionServiceTest extends GenericTest {
    @Mock
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        transactionService = new TransactionService(transactionRepository); // Crea la instancia del servicio
    }

    @Test
    void testGetTransactionHistory() {
        // Preparacion de la prueba
        double initialBalance = 20000.0;
        User user = getTestUser(initialBalance, null);
        Fund fund = getTestFund(null, 5000.0);

        // Datos de prueba
        String userId = "user123";
        Transaction transaction1 = new Transaction("tx123", user.getId(), fund.getId(), "investment789", "subscription", 10000, LocalDateTime.now());
        Transaction transaction2 = new Transaction("tx456", user.getId(), fund.getId(), "investment789", "subscription", 10000, LocalDateTime.now());
        Transaction transaction3 = new Transaction("tx789", user.getId(), fund.getId(), "investment789", "cancellation", 20000, LocalDateTime.now());
        List<Transaction> mockTransactions = Arrays.asList(transaction1, transaction2, transaction3);

        // Configuración del mock
        when(transactionRepository.findByUserId(userId)).thenReturn(mockTransactions);

        // Llamada al método
        List<Transaction> result = transactionService.getTransactionHistory(userId);

        // Verificación
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("tx123", result.get(0).getId());
        verify(transactionRepository, times(1)).findByUserId(userId); // Verifica que el repositorio fue llamado una vez
    }

    @Test
    void testGetTransactionsByType() {
        // Preparacion de la prueba
        double initialBalance = 20000.0;
        User user = getTestUser(initialBalance, null);
        Fund fund = getTestFund(null, 5000.0);

        // Datos de prueba
        String userId = "user123";
        Transaction transaction1 = new Transaction("tx123", user.getId(), fund.getId(), "investment789", "subscription", 10000, LocalDateTime.now());
        Transaction transaction2 = new Transaction("tx456", user.getId(), fund.getId(), "investment789", "subscription", 10000, LocalDateTime.now());
        List<Transaction> mockTransactions = Arrays.asList(transaction1, transaction2);


        // Configuración del mock
        when(transactionRepository.findByUserIdAndType(userId, "subscription")).thenReturn(mockTransactions);

        // Llamada al método
        List<Transaction> result = transactionService.getTransactionsByType(userId, "subscription");

        // Verificación
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("subscription", result.get(0).getType());
        verify(transactionRepository, times(1)).findByUserIdAndType(userId, "subscription"); // Verifica que el repositorio fue llamado una vez
    }
}
