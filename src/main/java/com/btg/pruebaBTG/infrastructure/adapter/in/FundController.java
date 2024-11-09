package com.btg.pruebaBTG.infrastructure.adapter.in;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btg.pruebaBTG.application.dto.SubscriptionRequest;
import com.btg.pruebaBTG.application.service.FundService;

@RestController
@RequestMapping("/api/funds")
public class FundController {

    private final FundService fundService;

    public FundController(FundService fundService) {
        this.fundService = fundService;
    }

    /**
     * Suscribir a un usuario a un fondo.
     * @param userId ID del usuario que se va a suscribir
     * @param fundId ID del fondo al que se va a suscribir
     * @param investmentAmount Monto a invertir en el fondo
     * @return Respuesta con éxito o error
     */
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribeToFund(@RequestBody SubscriptionRequest subscriptionRequest) {
        try {
            // Validar que investmentAmount no sea nulo
            if (Objects.isNull(subscriptionRequest.getInvestmentAmount())) {
                return ResponseEntity.badRequest().body("El monto de inversion no puede ser nulo o cero.");
            }

            // Validar que investmentAmount sea positivo (si no lo es, devolver error)
            if (subscriptionRequest.getInvestmentAmount() <= 0) {
                return ResponseEntity.badRequest().body("El monto de inversion debe ser mayor a cero.");
            }
            fundService.subscribeToFund(subscriptionRequest.getUserId(), subscriptionRequest.getFundId(), subscriptionRequest.getInvestmentAmount());
            return ResponseEntity.ok("Subscripción Exitosa.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Cancelar la suscripción de un usuario a un fondo.
     * @param userId ID del usuario que cancela la suscripción
     * @param fundId ID del fondo del que se cancela la suscripción
     * @return Respuesta con éxito o error
     */
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelSubscription(@RequestBody SubscriptionRequest subscriptionRequest) {
        try {
            fundService.cancelSubscription(subscriptionRequest.getUserId(), subscriptionRequest.getFundId());
            return ResponseEntity.ok("Cancelación exitosa.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}