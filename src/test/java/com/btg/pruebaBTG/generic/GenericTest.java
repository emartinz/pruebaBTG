package com.btg.pruebaBTG.generic;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.btg.pruebaBTG.domain.core.SesEmailService;
import com.btg.pruebaBTG.domain.core.SnsService;
import com.btg.pruebaBTG.domain.model.entities.Fund;
import com.btg.pruebaBTG.domain.model.entities.User;
import com.btg.pruebaBTG.infrastructure.adapter.out.FundRepository;
import com.btg.pruebaBTG.infrastructure.adapter.out.TransactionRepository;
import com.btg.pruebaBTG.infrastructure.adapter.out.UserFundInvestmentRepository;
import com.btg.pruebaBTG.infrastructure.adapter.out.UserRepository;

public class GenericTest {
    @Mock
    protected UserRepository userRepository;

    @Mock
    protected FundRepository fundRepository;

    @Mock
    protected TransactionRepository transactionRepository;

    @Mock
    protected UserFundInvestmentRepository userFundInvestmentRepository;

    @Mock
    protected SesEmailService sesEmailService;

    @Mock
    protected SnsService snsService;

    @BeforeEach
    protected void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa los mocks
    }

    /**
     * Método sobrecargado para obtener un usuario de prueba sin pasar parámetros
     * @return Usuario de Prueba
     */
    protected User getTestUser() {
        return getTestUser(null, null, null, null, null, null);
    }

    /**
     * Método sobrecargado para obtener un usuario de prueba pasando solo initialBalance y/o preferredNotification
     * @param initialBalance Saldo inicial
     * @param preferredNotification Medio de Notificacion preferido
     * @return Usuario de Prueba
     */
    protected User getTestUser(Double initialBalance, String preferredNotification) {
        return getTestUser(null, null, initialBalance, preferredNotification, null, null);
    }

    /**
     * Método para obtener un usuario de prueba
     * @param id Id
     * @param name Nombre del Usuario
     * @param initialBalance Saldo Inicial
     * @param preferredNotification Medio de Notificacion preferido
     * @param email Correo Electrónico
     * @param phoneNumber Numero de Celular
     * @return Usuario de Prueba
     */
    protected User getTestUser(String id, String name, Double initialBalance, String preferredNotification, String email, String phoneNumber) {
        User testUser = new User();
        testUser.setId(id != null ? id : "user123");
        testUser.setName(name != null ? name : "Usuario de Prueba");
        testUser.setEmail(email != null ? email : "testuser@example.com");
        testUser.setBalance(initialBalance != null ? initialBalance : 100000.0);
        testUser.setPreferredNotification(preferredNotification != null ? preferredNotification : "email");
        testUser.setPhoneNumber(phoneNumber != null? phoneNumber : "+573001234567");
        return testUser;
    }

    /**
     * Método sobrecargado para obtener un fondo de prueba sin pasar parámetros
     * @return Fondo de prueba
     */
    protected Fund getTestFund() {
        return getTestFund(null, null, null, null);
    }

    /**
     * Método sobrecargado para obtener un fondo de prueba pasando solo el nombre y el monto minimo
     * @param name Nombre del Fondo
     * @param minimumAmount Monto Minimo de Apertura
     * @return Fondo de prueba
     */
    protected Fund getTestFund(String name, Double minimumAmount) {
        return getTestFund(null, name, minimumAmount, null);
    }

    /**
     * Método para obtener un fondo de prueba
     * @param id Id
     * @param name Nombre del Fondo
     * @param minimumAmount Monto Minimo de Apertura
     * @param categoryName Categoria del Fondo
     * @return Fondo de prueba
     */
    protected Fund getTestFund(String id, String name, Double minimumAmount, String categoryName) {
        Fund testFund = new Fund();
        testFund.setId(id != null ? id : "fund456");
        testFund.setName(name != null ? name : "Test Fund");
        testFund.setMinimumAmount(minimumAmount != null ? minimumAmount : 15000.00);
        testFund.setCategory(categoryName != null? categoryName: "FIC");
        return testFund;
    }

}
