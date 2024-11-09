package com.btg.pruebaBTG.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.btg.pruebaBTG.domain.core.SesEmailService;
import com.btg.pruebaBTG.domain.core.SnsService;
import com.btg.pruebaBTG.domain.model.entities.Fund;
import com.btg.pruebaBTG.domain.model.entities.Transaction;
import com.btg.pruebaBTG.domain.model.entities.User;
import com.btg.pruebaBTG.domain.model.entities.UserFundInvestment;
import com.btg.pruebaBTG.domain.model.enums.PreferredNotificationType;
import com.btg.pruebaBTG.domain.model.enums.UserFundInvestmentStatus;
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
    private final SesEmailService sesEmailService;
    private final SnsService snsService;

    public FundService(UserRepository userRepository, FundRepository fundRepository, TransactionRepository transactionRepository, UserFundInvestmentRepository userFundInvestmentRepository, SesEmailService sesEmailService, SnsService snsService) {
        this.userRepository = userRepository;
        this.fundRepository = fundRepository;
        this.transactionRepository = transactionRepository;
        this.userFundInvestmentRepository = userFundInvestmentRepository;
        this.sesEmailService = sesEmailService;
        this.snsService = snsService;
    }

    /**
     * Método que maneja la subscripción de un usuario a un fondo específico.
     * @param userId Id del usuario que se subscribe al fondo específico.
     * @param fundId Id del fondo al que el usuario se subscribe
     * @param investmentAmount monto de inversion
     */
    @Transactional
    public void subscribeToFund(String userId, String fundId, double investmentAmount) {
        // Verifica si el usuario tiene saldo suficiente para suscribirse al fondo
        if (investmentAmount <= 0) {
            throw new RuntimeException("El monto a invertir no puede ser igual o inferior a cero.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        Fund fund = fundRepository.findById(fundId).orElseThrow(() -> new RuntimeException("Fondo no encontrado."));

        // Verifica si el usuario tiene saldo suficiente para suscribirse al fondo
        if (investmentAmount < fund.getMinimumAmount()) {
            throw new RuntimeException("El monto ingresado no es suficiente para vincularse al fondo " + fund.getName() + ". Monto minimo: " + fund.getMinimumAmount());
        }

        // Verifica si el usuario tiene saldo suficiente para invertir el monto especificado
        if (user.getBalance() < investmentAmount) {
            throw new RuntimeException("No tiene saldo disponible para vincularse al fondo " + fund.getName());
        }
        
        // Actualiza el saldo del usuario y guarda la transacción
        user.setBalance(user.getBalance() - investmentAmount);
        userRepository.save(user);

        // Busca o crea un registro en UserFundInvestment para el usuario y el fondo
        UserFundInvestment investment = userFundInvestmentRepository.findByUserIdAndFundIdAndStatus(userId, fundId, UserFundInvestmentStatus.ACTIVE)
        .orElse(new UserFundInvestment(null, userId, fundId, 0, UserFundInvestmentStatus.ACTIVE));

        // Actualiza el monto total de inversión
        investment.setInvestmentAmount(investment.getInvestmentAmount() + investmentAmount);
        userFundInvestmentRepository.save(investment);

        // Guarda un registro de la transacción
        Transaction transaction = new Transaction(null, userId, fundId, investment.getId(), "subscription", investmentAmount, LocalDateTime.now());
        transactionRepository.save(transaction);

        // Envía una notificación al usuario
        sendNotification(user, "Subscripcion", "La subscripcion al fondo " + fund.getName() + " se realizó correctamente.");
    }

    /**
     * Método que cancela la subscripción de un usuario a un fondo específico.
     * @param userId Id del usuario que cancela la subscripción.
     * @param fundId Id del fondo al cual el usuario se va a desubscribir.
     */
    @Transactional
    public void cancelSubscription(String userId, String fundId) {
        Fund fund = fundRepository.findById(fundId).orElseThrow(() -> new RuntimeException("Fondo no encontrado."));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        UserFundInvestment investment = userFundInvestmentRepository.findByUserIdAndFundIdAndStatus(userId, fundId, UserFundInvestmentStatus.ACTIVE)
            .orElseThrow(() -> new RuntimeException("El usuario " + user.getName() + " no está suscrito al fondo " + fund.getName()));
        
        // Actualiza el saldo del usuario y guarda la transacción de cancelación
        user.setBalance(user.getBalance() + investment.getInvestmentAmount());
        userRepository.save(user);

        // Guarda un registro de la transacción
        Transaction transaction = new Transaction(null, userId, fundId, investment.getId(), "cancellation", investment.getInvestmentAmount(), LocalDateTime.now());
        transactionRepository.save(transaction);

        // Actualiza el monto total de inversión
        investment.setInvestmentAmount(0);
        investment.setStatus(UserFundInvestmentStatus.CANCELLED);
        userFundInvestmentRepository.save(investment);

        // Envía una notificación al usuario
        sendNotification(user, "Cancelacion", "La cancelacion de la subscripcion al fondo " + fund.getName() + " se realizó correctamente.");
    }

    /**
     * Método que envía una notificacion a los usuarios basándose en las preferencias de notificación del usuario.
     * @param user Usuario al que se enviará la notificación.
     * @param message Mensaje que se enviará al usuario.
     */
    private void sendNotification(User user, String subject, String message) {
        if (PreferredNotificationType.EMAIL.equals(user.getPreferredNotification())) {
            sendEmail(user.getEmail(), subject, message);
        } else if (PreferredNotificationType.SMS.equals(user.getPreferredNotification())) {
            sendSms(user.getPhoneNumber(), message);
        } else {
            System.out.println("La notificación no puede ser enviada: No se especificó un medio de notificación válido.");
        }
    }

    /**
     * Método para enviar mensaje por correo.
     * @param email
     * @param subject
     * @param message
     */
    private void sendEmail(String email, String subject,  String message) {
        System.out.println("Enviando correo a " + email + ": " + message);
        sesEmailService.sendEmail(email, subject, message);
    }

    /**
     * Método para enviar mensaje vía SMS.
     * @param phoneNumber
     * @param message
     */
    private void sendSms(String phoneNumber, String message) {
        System.out.println("Enviando SMS a " + phoneNumber + ": " + message);
        snsService.sendSms(phoneNumber, message);
    }
}