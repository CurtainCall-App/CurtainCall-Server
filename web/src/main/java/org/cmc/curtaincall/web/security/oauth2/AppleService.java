package org.cmc.curtaincall.web.security.oauth2;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.exception.AuthenticationException;
import org.cmc.curtaincall.web.security.response.AppleDecodedPayload;
import org.cmc.curtaincall.web.security.response.ApplePublicKeyResponse;
import org.cmc.curtaincall.web.security.response.ApplePublicKeysResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AppleService {


    /**
     * 1. apple로 부터 공개키 3개 가져옴
     * 2. 내가 클라에서 가져온 token String과 비교해서 써야할 공개키 확인 (kid,alg 값 같은 것)
     * 3. 그 공개키 재료들로 공개키 만들고, 이 공개키로 JWT토큰 부분의 바디 부분의 decode하면 유저 정보
     */
    public String userIdFromApple(String idToken) {
        ApplePublicKeysResponse keysResponse = getPublicKeys();

        String[] tokenSplit = idToken.split("\\.");
        Header header = Jwts.parserBuilder().build()
                .parseClaimsJwt(tokenSplit[0] + "." + tokenSplit[1] + ".").getHeader();
        String kid = (String) header.get("kid");
        ApplePublicKeyResponse keyResponse = keysResponse.keys().stream()
                .filter(key -> key.kid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new AuthenticationException(
                        String.format("kid=%s, %s", kid, keysResponse))
                );

        AppleDecodedPayload tokenBody = getTokenBody(idToken, keyResponse.n(), keyResponse.e());

        return tokenBody.email();
    }

    private ApplePublicKeysResponse getPublicKeys() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                "https://appleid.apple.com/auth/keys",
                HttpMethod.GET,
                null,
                ApplePublicKeysResponse.class
        ).getBody();
    }

    private AppleDecodedPayload getTokenBody(String token, String modulus, String exponent) {
        Claims body = getJws(token, modulus, exponent).getBody();
        return AppleDecodedPayload.builder()
                .iss(body.getIssuer())
                .aud(body.getAudience())
                .sub(body.getSubject())
                .email(body.get("email", String.class))
                .build();
    }

    private Jws<Claims> getJws(String token, String modulus, String exponent) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getRSAPublicKey(modulus, exponent))
                    .build()
                    .parseClaimsJws(token);
        } catch (RuntimeException e) {
            throw new AuthenticationException(
                    String.format("token=%s, modulus=%s, exponent=%s", token, modulus, exponent), e);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AuthenticationException("NoSuchAlgorithmException | InvalidKeySpecException", e);
        }
    }

    private Key getRSAPublicKey(String modulus, String exponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        return keyFactory.generatePublic(keySpec);
    }
}
