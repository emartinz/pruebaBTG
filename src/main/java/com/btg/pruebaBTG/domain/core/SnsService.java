package com.btg.pruebaBTG.domain.core;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

@Service
public class SnsService {

    private final SnsClient snsClient;

    // Inyecta el valor de topicArn desde el archivo de configuración o variable de entorno
    public SnsService() {
        this.snsClient = SnsClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    // Método para enviar un SMS directamente a un número de teléfono
    public void sendSms(String phoneNumber, String message) {
        PublishRequest request = PublishRequest.builder()
                .message(message)
                .phoneNumber(phoneNumber)
                .build();

        try {
            PublishResponse result = snsClient.publish(request);
            System.out.println("SMS sent! Message ID: " + result.messageId());
        } catch (SnsException e) {
            System.err.println("Failed to send SMS: " + e.awsErrorDetails().errorMessage());
        }
    }
}
