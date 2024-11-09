package com.btg.pruebaBTG.domain.core;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SesEmailService {

    private final SesClient sesClient;
    private final String sourceEmail;

    // Inyecta el valor de sourceEmail desde el archivo de configuración o variable de entorno
    public SesEmailService(@Value("${aws.ses.sourceEmail}") String sourceEmail) {
        this.sesClient = SesClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        this.sourceEmail = sourceEmail;
    }

    /**
     * Método para envíar correos
     * @param toAddress Destinatario
     * @param subject   Asunto
     * @param bodyText  Mensaje
     */
    public void sendEmail(String toAddress, String subject, String bodyText) {
        // Crear el mensaje de correo electrónico
        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses(toAddress).build())
                .message(Message.builder()
                        .subject(Content.builder().data(subject).build())
                        .body(Body.builder()
                                .text(Content.builder().data(bodyText).build())
                                .build())
                        .build())
                .source(sourceEmail) // Usar el correo de origen inyectado
                .build();

        // Enviar el correo electrónico
        try {
            SendEmailResponse response = sesClient.sendEmail(emailRequest);
            System.out.println("Email enviado! Message ID: " + response.messageId());
        } catch (SesException e) {
            System.err.println("Ocurrió un erro al enviar correo a: " + e.awsErrorDetails().errorMessage());
        }
    }
}