package com.btg.pruebaBTG.application.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.btg.pruebaBTG.domain.model.entities.Fund;
import com.btg.pruebaBTG.domain.model.entities.Transaction;
import com.btg.pruebaBTG.domain.model.entities.User;
import com.btg.pruebaBTG.domain.model.entities.UserFundInvestment;
import com.btg.pruebaBTG.infrastructure.adapter.out.FundRepository;
import com.btg.pruebaBTG.infrastructure.adapter.out.TransactionRepository;
import com.btg.pruebaBTG.infrastructure.adapter.out.UserFundInvestmentRepository;
import com.btg.pruebaBTG.infrastructure.adapter.out.UserRepository;

@Service
public class FundService {
    private final UserRepository userRepository;
    private final FundRepository fundRepository;
    private final TransactionRepository transactionRepository;
    private final UserFundInvestmentRepository userFundInvestmentRepository;

    public FundService(UserRepository userRepository, FundRepository fundRepository, TransactionRepository transactionRepository, UserFundInvestmentRepository userFundInvestmentRepository) {
        this.userRepository = userRepository;
        this.fundRepository = fundRepository;
        this.transactionRepository = transactionRepository;
        this.userFundInvestmentRepository = userFundInvestmentRepository;
    }

    /**
     * Método que maneja la subscripción de un usuario a un fondo específico.
     * @param userId Id del usuario que se subscribe al fondo específico.
     * @param fundId Id del fondo al que el usuario se subscribe
     * @param investmentAmount monto de inversion
     */
    public void subscribeToFund(String userId, String fundId, double investmentAmount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Fund fund = fundRepository.findById(fundId).orElseThrow(() -> new RuntimeException("Fund not found"));

        // Verifica si el usuario tiene saldo suficiente para suscribirse al fondo
        if (user.getBalance() < fund.getMinimumAmount()) {
            throw new RuntimeException("Insufficient balance to subscribe to the fund " + fund.getName());
        }

        // Verifica si el usuario tiene saldo suficiente para invertir el monto especificado
        if (user.getBalance() < investmentAmount) {
            throw new RuntimeException("Insufficient balance to invest " + investmentAmount + " in the fund " + fund.getName());
        }
        
        // Actualiza el saldo del usuario y guarda la transacción
        user.setBalance(user.getBalance() - investmentAmount);
        userRepository.save(user);

        // Busca o crea un registro en UserFundInvestment para el usuario y el fondo
        UserFundInvestment investment = userFundInvestmentRepository.findByUserIdAndFundId(userId, fundId)
        .orElse(new UserFundInvestment(null, userId, fundId, 0));

        // Actualiza el monto total de inversión
        investment.setInvestmentAmount(investment.getInvestmentAmount() + investmentAmount);
        userFundInvestmentRepository.save(investment);

        // Guarda un registro de la transacción
        Transaction transaction = new Transaction(null, userId, fundId, "subscription", investment.getInvestmentAmount(), LocalDateTime.now());
        transactionRepository.save(transaction);

        // Envía una notificación al usuario
        sendNotification(user, "Subscription successful to the fund " + fund.getName());
    }

    /**
     * Método que cancela la subscripción de un usuario a un fondo específico.
     * @param userId Id del usuario que cancela la subscripción.
     * @param fundId Id del fondo al cual el usuario se va a desubscribir.
     */
    public void cancelSubscription(String userId, String fundId) {
        Fund fund = fundRepository.findById(fundId).orElseThrow(() -> new RuntimeException("Fund not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        
        // Actualiza el saldo del usuario y guarda la transacción de cancelación
        user.setBalance(user.getBalance() + fund.getMinimumAmount());
        userRepository.save(user);

        Transaction transaction = new Transaction(null, userId, fundId, "cancellation", fund.getMinimumAmount(), LocalDateTime.now());
        transactionRepository.save(transaction);

        // Envía una notificación al usuario
        sendNotification(user, "Cancellation successful for the fund " + fund.getName());
    }

    /**
     * Método que obtiene el historial de transacciones de un usuario específico.
     * @param userId Id del usuario del cual se quiere obtener el historial.
     * @return Lista del historial de transacciones del usuario específicado.
     */
    public List<Transaction> getTransactionHistory(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    /**
     * Método que envía una notificacion a los usuarios basándose en las preferencias de notificación del usuario.
     * @param user Usuario al que se enviará la notificación.
     * @param message Mensaje que se enviará al usuario.
     */
    private void sendNotification(User user, String message) {
        if ("email".equalsIgnoreCase(user.getPreferredNotification())) {
            sendEmail(user.getEmail(), message);
        } else if ("sms".equalsIgnoreCase(user.getPreferredNotification())) {
            sendSms(user.getPhoneNumber(), message);
        } else {
            System.out.println("Notification could not be sent: no preference specified.");
        }
    }

    /**
     * Método para enviar mensaje por correo.
     * @param email
     * @param message
     */
    private void sendEmail(String email, String message) {
        // TODO: Lógica para enviar el email
        System.out.println("Sending email to " + email + ": " + message);
    }

    /**
     * Método para enviar mensaje vía SMS.
     * @param phoneNumber
     * @param message
     */
    private void sendSms(String phoneNumber, String message) {
        // TODO: Lógica para enviar SMS
        System.out.println("Sending SMS to " + phoneNumber + ": " + message);
    }
}