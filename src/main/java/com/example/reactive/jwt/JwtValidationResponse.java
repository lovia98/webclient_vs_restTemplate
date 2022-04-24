package com.example.reactive.jwt;

import lombok.Getter;

import java.util.Map;

/**
 * jwt validation 응답 객체
 */
@Getter
public class JwtValidationResponse {

    private boolean isSuccess;
    private Map<String, String> claim;
    private String errCode;
    private String errMessage;

    /**
     * jwt 토큰 인증 성공 생성자
     *
     * @param isSuccess
     */
    public JwtValidationResponse(boolean isSuccess, Map<String, String> claim) {
        this.isSuccess = isSuccess;
        this.claim = claim;
    }

    /**
     * jwt 토큰 인증 실패 생성자
     *
     * @param errCode
     * @param errMessage
     */
    public JwtValidationResponse(String errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }
}
