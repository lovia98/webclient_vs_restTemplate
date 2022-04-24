package com.example.reactive.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Slf4j
@UtilityClass
public class JwtUtils {

    private static final String BEARER = "Bearer ";

    private final Base64.Decoder decoder = Base64.getDecoder();
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    private static final String commonJwtSecret = "tkaruqtkf(@#"; //jwt salt key
    private static final String tokenIssuser = "ToastCam"; //토큰 발행인

    private static Algorithm ALGORITHM_512_COMMON;

    static {
        ALGORITHM_512_COMMON = Algorithm.HMAC512(commonJwtSecret);
    }

    /**
     * 로그인 2차 인증 번호 생성 토큰
     *
     * @param userId     사용자 아이디
     * @param userRole   사용자 권한 코드
     * @param profile    profile
     * @param authNumber 인증번호
     * @return
     */
    public String createAuthNumberToken(String userId, String userRole, String profile, String authNumber) {
        try {
            Date expireDate = DateUtils.addMinutes(new Date(), 3); //3분 유효기간

            String token = JWT.create()
                    .withIssuer(tokenIssuser)
                    .withClaim("userId", userId)
                    .withClaim("userRole", userRole)
                    .withClaim("profile", profile)
                    .withClaim("authNumber", Sha256.encrypt(authNumber))
                    .withJWTId(RandomStringUtils.randomAlphabetic(10)) //jwt 토큰 자리수
                    .withIssuedAt(new Date())
                    .withExpiresAt(expireDate)
                    .sign(ALGORITHM_512_COMMON);

            return token;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 2차 로그인 인증번호 체크
     *
     * @param token
     * @return
     */
    public JwtValidationResponse validationAuthNumberToken(String token, String authNumber) {
        JwtValidationResponse jwtValidationResponse = validationToken(token);

        //토큰 validation 후 인증번호가 올바른지 확인
        if (!jwtValidationResponse.isSuccess() || jwtValidationResponse.getClaim() == null || StringUtils.isBlank(jwtValidationResponse.getClaim().get("authNumber"))) {
            return new JwtValidationResponse("INVALID AUTH NUMBER", "인증번호 틀림");
        }

        String compare = jwtValidationResponse.getClaim().get("authNumber");
        Sha256.encrypt(authNumber);

        if (StringUtils.equals(compare, Sha256.encrypt(authNumber))) {
            return jwtValidationResponse;
        } else {
            return new JwtValidationResponse("INVALID AUTH NUMBER", "인증번호 틀림");
        }
    }

    /**
     * jwt 토큰 validation
     *
     * @param token
     * @return
     */
    private JwtValidationResponse validationToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return new JwtValidationResponse("TOKEN EMPTY", "jwt token can not be null or empty");
        }

        token = token.replaceFirst(BEARER, "");

        if (StringUtils.isBlank(token)) {
            return new JwtValidationResponse("TOKEN EMPTY", "jwt token can not be null or empty");
        }

        final String errLogMsg = "jwt token validation exception";
        String errDetail = "";

        try {
            JWTVerifier verifier = JWT.require(ALGORITHM_512_COMMON)
                    .withIssuer(tokenIssuser)
                    .build();
            // verify 과정 중에 토큰 자체의 유효성은 검증됨.
            DecodedJWT verify = verifier.verify(token);

            // 페이로드 추출.
            String payload = new String(decoder.decode(verify.getPayload()));
            Map<String, String> payLoadMap = gson.fromJson(payload, new TypeToken<Map<String, String>>() {
            }.getType());

            return new JwtValidationResponse(true, payLoadMap);

        } catch (TokenExpiredException ex) {
            log.info("{} : 토큰 유효시간 지남 : {}, token: {}", errLogMsg, ex.getMessage(), token);
            return new JwtValidationResponse("PASSED EXPIRE TIME", "passed expire time.");
        } catch (SignatureVerificationException ex) {
            log.info("{} : SignatureVerificationException message: {}, token: {}", errLogMsg, ex.getMessage(), token);
        } catch (JWTVerificationException ex) {
            log.info("{} : JWTVerificationException message: {}, token: {}", ex.getMessage(), token);
        } catch (Exception ex) {
            log.error("{}. token: {}, stackTrace: {}", errLogMsg, token, ExceptionUtils.getStackTrace(ex));
        }

        return new JwtValidationResponse("INVALID_JWT_TOKEN", errDetail);
    }

}
