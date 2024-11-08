package com.btg.pruebaBTG;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import com.btg.pruebaBTG.application.service.FundService;
import com.btg.pruebaBTG.domain.model.entities.Fund;
import com.btg.pruebaBTG.domain.model.entities.Transaction;
import com.btg.pruebaBTG.domain.model.entities.User;
import com.btg.pruebaBTG.domain.model.entities.UserFundInvestment;
import com.btg.pruebaBTG.domain.model.enums.UserFundInvestmentStatus;
import com.btg.pruebaBTG.generic.GenericTest;

class FundServiceTest extends GenericTest {
    @InjectMocks
    private FundService fundService;

    @Test
    void testSubscribeToFund_successfulSubscription() {
        // Preparacion de la prueba
        double initialBalance = 20000.0;
        double investmentAmount = 10000.0;
        User user = getTestUser(initialBalance, null);
        Fund fund = getTestFund(null, 5000.0);
        UserFundInvestment newInvestment = new UserFundInvestment(null, user.getId(), fund.getId(), 0, UserFundInvestmentStatus.ACTIVE);

        // Mock de repositorios
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(fundRepository.findById(fund.getId())).thenReturn(Optional.of(fund));
        
        when(userFundInvestmentRepository.findByUserIdAndFundIdAndStatus(user.getId(), fund.getId(), UserFundInvestmentStatus.ACTIVE))
        .thenReturn(Optional.of(newInvestment));        

        // Llama al método
        fundService.subscribeToFund(user.getId(), fund.getId(), investmentAmount);

        // Verificar los resultados
        assertEquals(investmentAmount, newInvestment.getInvestmentAmount());
        assertEquals(user.getBalance(), initialBalance - investmentAmount);

        verify(userRepository).save(user);
        verify(transactionRepository).save(any(Transaction.class)); // Verifica que se cree la transaccion
    }

    @Test
    void testSubscribeToFund_successfulSubscriptionToExistingInvestment() {
        // Preparacion de la prueba
        double initialBalance = 20000.0;
        double investmentAmount = 10000.0;
        User user = getTestUser(initialBalance, null);
        Fund fund = getTestFund(null, 5000.0);
        UserFundInvestment existingInvestment = new UserFundInvestment(null, user.getId(), fund.getId(), 10000.0, UserFundInvestmentStatus.ACTIVE);
        double initialInvestmentAmount = existingInvestment.getInvestmentAmount();

        // Mock de repositorios
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(fundRepository.findById(fund.getId())).thenReturn(Optional.of(fund));
        when(userFundInvestmentRepository.findByUserIdAndFundIdAndStatus(user.getId(), fund.getId(), UserFundInvestmentStatus.ACTIVE))
            .thenReturn(Optional.of(existingInvestment));

        // Llama al método
        fundService.subscribeToFund(user.getId(), fund.getId(), investmentAmount);

        // Verificar los resultados
        assertEquals(initialInvestmentAmount + investmentAmount, existingInvestment.getInvestmentAmount());
        assertEquals(user.getBalance(), initialBalance - investmentAmount);

        verify(userRepository).save(user);
        verify(transactionRepository).save(any(Transaction.class)); // Verifica que se cree la transaccion
    }
    
    @Test
    void testSubscribeToFund_insufficientBalance() {
        // Preparacion de la prueba
        double initialBalance = 10000.0;
        double investmentAmount = 15000.0;
        User user = getTestUser(initialBalance, null);
        Fund fund = getTestFund(null, 15000.0);

        // Mock de repositorios
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(fundRepository.findById(fund.getId())).thenReturn(Optional.of(fund));

        // Llamar al método y verificar excepcion
        Exception exception = assertThrows(RuntimeException.class, () -> {
            fundService.subscribeToFund(user.getId(), fund.getId(), investmentAmount);
        });

        assertEquals("No tiene saldo disponible para vincularse al fondo " + fund.getName(), exception.getMessage());
        verify(userRepository, never()).save(user);
        verify(transactionRepository, never()).save(any(Transaction.class)); // Verifica que se cree la transaccion
    }

    @Test
    void testSubscribeToFund_negativeAmmount() {
        // Preparacion de la prueba
        double initialBalance = 10000.0;
        double investmentAmount = -15000.0;
        User user = getTestUser(initialBalance, null);
        Fund fund = getTestFund(null, 15000.0);

        // Mock de repositorios
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(fundRepository.findById(fund.getId())).thenReturn(Optional.of(fund));

        // Llamar al método y verificar excepcion
        Exception exception = assertThrows(RuntimeException.class, () -> {
            fundService.subscribeToFund(user.getId(), fund.getId(), investmentAmount);
        });

        assertEquals("El monto a invertir no puede ser igual o inferior a cero.", exception.getMessage());
        verify(userRepository, never()).save(user);
        verify(transactionRepository, never()).save(any(Transaction.class)); // Verifica que no se cree la transaccion
    }

    @Test
    void testSubscribeToFund_minimunAmmount() {
        // Preparacion de la prueba
        double initialBalance = 100000.0;
        double investmentAmount = 10000.0;
        User user = getTestUser(initialBalance, null);
        Fund fund = getTestFund(null, 15000.0);

        // Mock de repositorios
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(fundRepository.findById(fund.getId())).thenReturn(Optional.of(fund));

        // Llamar al método y verificar excepcion
        Exception exception = assertThrows(RuntimeException.class, () -> {
            fundService.subscribeToFund(user.getId(), fund.getId(), investmentAmount);
        });

        assertEquals("El monto ingresado no es suficiente para vincularse al fondo " + fund.getName() + ". Monto minimo: " + fund.getMinimumAmount(), exception.getMessage());
        verify(userRepository, never()).save(user); 
        verify(transactionRepository, never()).save(any(Transaction.class)); // Verifica que no se cree la transaccion
    }

    
    @Test
    void testSubscribeToFund_successfulCancellation() {
        // Preparacion de la prueba
        double initialBalance = 20000.0;
        User user = getTestUser(initialBalance, null);
        Fund fund = getTestFund(null, 5000.0);
        UserFundInvestment existingInvestment = new UserFundInvestment(null, user.getId(), fund.getId(), 10000.0, UserFundInvestmentStatus.ACTIVE);
        double initialInvestmentAmount = existingInvestment.getInvestmentAmount();
 
        // Mock de repositorios
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(fundRepository.findById(fund.getId())).thenReturn(Optional.of(fund));
        when(userFundInvestmentRepository.findByUserIdAndFundIdAndStatus(user.getId(), fund.getId(), UserFundInvestmentStatus.ACTIVE))
            .thenReturn(Optional.of(existingInvestment));
 
        // Llama al método
        fundService.cancelSubscription(user.getId(), fund.getId());
 
        // Verificar los resultados
        assertEquals(0, existingInvestment.getInvestmentAmount());
        assertEquals(UserFundInvestmentStatus.CANCELLED, existingInvestment.getStatus());
        assertEquals(user.getBalance(), initialBalance + initialInvestmentAmount);
 
        verify(userRepository).save(user);
        verify(transactionRepository).save(any(Transaction.class)); // Verifica que se cree la transaccion
    }

    @Test
    void testSubscribeToFund_fundAlreadyCancelled() {
        // Preparacion de la prueba
        double initialBalance = 20000.0;
        User user = getTestUser(initialBalance, null);
        Fund fund = getTestFund(null, 5000.0);
        UserFundInvestment existingInvestment = new UserFundInvestment(null, user.getId(), fund.getId(), 0.0, UserFundInvestmentStatus.CANCELLED);

        // Mock de repositorios
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(fundRepository.findById(fund.getId())).thenReturn(Optional.of(fund));
        when(userFundInvestmentRepository.findByUserIdAndFundIdAndStatus(user.getId(), fund.getId(), UserFundInvestmentStatus.ACTIVE))
            .thenReturn(Optional.empty());
 
        // Llamar al método y verificar excepcion
        Exception exception = assertThrows(RuntimeException.class, () -> {
            fundService.cancelSubscription(user.getId(), fund.getId());
        });

        // Verificar los resultados
        assertEquals(0, existingInvestment.getInvestmentAmount());
        assertEquals("El usuario " + user.getName() + " no está suscrito al fondo " + fund.getName(), exception.getMessage());
        assertEquals(user.getBalance(), initialBalance);
        verify(userRepository, never()).save(user); 
        verify(transactionRepository, never()).save(any(Transaction.class)); // Verifica que no se cree la transaccion
    }

}