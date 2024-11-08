package com.btg.pruebaBTG.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AwsConfig {

    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder()
                        .credentialsProvider(DefaultCredentialsProvider.create()) // Cargar credenciales desde .aws
                        .region(Region.US_EAST_1)
                        .build();
    }

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                        .credentialsProvider(DefaultCredentialsProvider.create()) // Cargar credenciales desde .aws
                        .region(Region.US_EAST_1)
                        .build();
    }
}