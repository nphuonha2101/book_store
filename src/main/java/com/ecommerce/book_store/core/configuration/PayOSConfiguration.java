package com.ecommerce.book_store.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
public class PayOSConfiguration {
    @Value("${payment.payos.client-id:}")
    private String clientId;

    @Value("${payment.payos.api-key:}")
    private String apiKey;

    @Value("${payment.payos.checksum-key:}")
    private String checksumKey;

    @Bean
    public PayOS payOS() {
        return new PayOS(clientId, apiKey, checksumKey);
    }
}
