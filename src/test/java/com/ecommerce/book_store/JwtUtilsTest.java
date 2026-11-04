package com.ecommerce.book_store;

import com.ecommerce.book_store.core.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secretKey",
                "dGVzdC1vbmx5LWJvb2stc3RvcmUtc2VjcmV0LWtleS0xMjM0NTY3ODkwMTIzNDU2Nzg5MA==");
        ReflectionTestUtils.setField(jwtUtils, "expirationTime", 3_600_000L);
        jwtUtils.validateConfiguration();
    }

    @Test
    void generatesAndReadsToken() {
        String token = jwtUtils.generateToken("reader@example.com");

        assertEquals("reader@example.com", jwtUtils.extractUserEmail(token));
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void rejectsTamperedToken() {
        String token = jwtUtils.generateToken("reader@example.com");
        String tampered = token.substring(0, token.length() - 1) + "x";

        assertFalse(jwtUtils.validateToken(tampered));
    }

    @Test
    void rejectsShortSecret() {
        JwtUtils weak = new JwtUtils();
        ReflectionTestUtils.setField(weak, "secretKey", "c2hvcnQ=");
        ReflectionTestUtils.setField(weak, "expirationTime", 1_000L);

        assertThrows(IllegalStateException.class, weak::validateConfiguration);
    }
}
