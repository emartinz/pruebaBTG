package com.btg.pruebaBTG.infrastructure.adapter.in;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> subscribeToFund(
            @RequestParam String userId,
            @RequestParam String fundId,
            @RequestParam double investmentAmount) {
        try {
            fundService.subscribeToFund(userId, fundId, investmentAmount);
            return ResponseEntity.ok("Subscription successful.");
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
    public ResponseEntity<String> cancelSubscription(
            @RequestParam String userId,
            @RequestParam String fundId) {
        try {
            fundService.cancelSubscription(userId, fundId);
            return ResponseEntity.ok("Cancellation successful.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}