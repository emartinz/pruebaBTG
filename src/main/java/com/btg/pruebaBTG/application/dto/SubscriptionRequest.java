package com.btg.pruebaBTG.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionRequest {
    @NotBlank(message = "User ID is required.")
    private String userId;

    @NotBlank(message = "Fund ID is required.")
    private String fundId;

    @Positive(message = "Investment amount must be greater than zero.")
    private double investmentAmount;
}
