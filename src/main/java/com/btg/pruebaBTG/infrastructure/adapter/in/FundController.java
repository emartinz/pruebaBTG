package com.btg.pruebaBTG.infrastructure.adapter.in;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btg.pruebaBTG.application.dto.ApiResponse;
import com.btg.pruebaBTG.application.dto.SubscriptionRequest;
import com.btg.pruebaBTG.application.service.FundService;
import com.btg.pruebaBTG.domain.model.entities.Fund;

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
    public ResponseEntity<ApiResponse<Object>> subscribeToFund(@RequestBody SubscriptionRequest subscriptionRequest) {
        try {
            // Validar que investmentAmount no sea nulo
            if (Objects.isNull(subscriptionRequest.getInvestmentAmount())) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "error", 
                    "El monto de inversión no puede ser nulo o cero.", 
                    null
                ));
            }

            // Validar que investmentAmount sea positivo (si no lo es, devolver error)
            if (subscriptionRequest.getInvestmentAmount() <= 0) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(
                    "error", 
                    "El monto de inversión debe ser mayor a cero.",
                    null
                ));
            }
            fundService.subscribeToFund(subscriptionRequest.getUserId(), subscriptionRequest.getFundId(), subscriptionRequest.getInvestmentAmount());
            return ResponseEntity.ok(new ApiResponse<>("success", "Subscripción exitosa.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>("error", e.getMessage(), null));
        }
    }

    /**
     * Cancelar la suscripción de un usuario a un fondo.
     * @param userId ID del usuario que cancela la suscripción
     * @param fundId ID del fondo del que se cancela la suscripción
     * @return Respuesta con éxito o error
     */
    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<Object>> cancelSubscription(@RequestBody SubscriptionRequest subscriptionRequest) {
        try {
            fundService.cancelSubscription(subscriptionRequest.getUserId(), subscriptionRequest.getFundId());
            return ResponseEntity.ok(new ApiResponse<>(
                "success", 
                "Cancelación exitosa.",
                null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(
                "error", e.getMessage(), null
            ));
        }
    }
    
    /**
     * Método para obtener lista de Fondos disponibles
     * @param entity
     * @return
     */
    @GetMapping("/getList")
    public ResponseEntity<ApiResponse<List<Fund>>> getList() {
        List<Fund> funds = fundService.getFundsList();
    
        // Verifica si la lista está vacía y responde con HTTP 204 No Content
        if (funds.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(new ApiResponse<>("success", "Fondos encontrados.", funds));
    }
}