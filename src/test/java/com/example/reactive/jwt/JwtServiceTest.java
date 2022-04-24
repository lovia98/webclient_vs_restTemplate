package com.example.reactive.jwt;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    @DisplayName("인증번호 토큰")
    void createAuthNumberToken() {
        String authNumber = RandomStringUtils.randomNumeric(6); //인증번호

        String authNumberToken = JwtUtils.createAuthNumberToken("userId", "CUSTOMER", "dev", authNumber);
        assertNotNull(authNumberToken);

        JwtValidationResponse validAuthNumber1 = JwtUtils.validationAuthNumberToken(authNumberToken, "123456");
        assertFalse(validAuthNumber1.isSuccess());

        JwtValidationResponse validAuthNumber2 = JwtUtils.validationAuthNumberToken(authNumberToken, authNumber);
        assertTrue(validAuthNumber2.isSuccess());
    }

    @Test
    void create_secret_key() {
        System.out.println(RandomStringUtils.randomAlphabetic(6));
    }
}