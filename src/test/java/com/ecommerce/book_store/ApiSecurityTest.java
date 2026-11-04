package com.ecommerce.book_store;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicCatalogueIsAccessible() throws Exception {
        mockMvc.perform(get("/api/v1/books/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void protectedEndpointReturnsJsonUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.statusCode").value(401));
    }

    @Test
    void invalidLoginPayloadIsRejected() throws Exception {
        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"not-an-email\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void invalidPayOsSignatureIsRejected() throws Exception {
        String payload = """
                {
                  "code": "00",
                  "desc": "success",
                  "success": true,
                  "signature": "invalid-signature",
                  "data": {
                    "orderCode": 1,
                    "amount": 10000,
                    "description": "NPBookStore #1",
                    "accountNumber": "123",
                    "reference": "ref",
                    "transactionDateTime": "2026-01-01 00:00:00",
                    "currency": "VND",
                    "paymentLinkId": "link",
                    "code": "00",
                    "desc": "success"
                  }
                }
                """;

        mockMvc.perform(post("/api/v1/webhook/payos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }
}
