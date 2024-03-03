package org.cmc.curtaincall.web.security.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.cmc.curtaincall.web.security.service.CurtainCallJwtEncoderService;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
@EnableConfigurationProperties({CurtainCallJwtProperties.class, OAuth2ClientProperties.class})
@ConfigurationPropertiesScan
public class CurtainCallJwtConfig {

    @Bean
    public OctetSequenceKey octetSequenceKey(CurtainCallJwtProperties properties) throws JOSEException {
        return new OctetSequenceKeyGenerator(256)
                .keyID(properties.getSecret())
                .algorithm(JWSAlgorithm.HS256)
                .generate();
    }

    @Bean
    public JwtEncoder curtainCallJwtEncoder(OctetSequenceKey octetSequenceKey) {
        JWKSource<SecurityContext> secret = new ImmutableSecret<>(octetSequenceKey.toSecretKey());
        return new NimbusJwtEncoder(secret);
    }

    @Bean
    public JwtDecoder curtainCallJwtDecoder(OctetSequenceKey octetSequenceKey) {
        return NimbusJwtDecoder.withSecretKey(octetSequenceKey.toSecretKey())
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public OctetSequenceKey refreshTokenOctetSequenceKey(
            final CurtainCallJwtProperties properties
    ) throws JOSEException {
        return new OctetSequenceKeyGenerator(512)
                .keyID(properties.getSecret())
                .algorithm(JWSAlgorithm.HS512)
                .generate();
    }

    @Bean
    public JwtEncoder curtainCallJwtRefreshTokenEncoder(
            final OctetSequenceKey refreshTokenOctetSequenceKey
    ) {
        JWKSource<SecurityContext> secret = new ImmutableSecret<>(refreshTokenOctetSequenceKey.toSecretKey());
        return new NimbusJwtEncoder(secret);
    }

    @Bean
    public JwtDecoder curtainCallJwtRefreshTokenDecoder(final OctetSequenceKey refreshTokenOctetSequenceKey) {
        return NimbusJwtDecoder.withSecretKey(refreshTokenOctetSequenceKey.toSecretKey())
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    public CurtainCallJwtEncoderService curtainCallJwtEncoderService(
            final CurtainCallJwtProperties properties,
            final JwtEncoder curtainCallJwtEncoder,
            final JwtEncoder curtainCallJwtRefreshTokenEncoder
    ) {
        return new CurtainCallJwtEncoderService(
                curtainCallJwtEncoder,
                curtainCallJwtRefreshTokenEncoder,
                properties.getAccessTokenValidity(),
                properties.getRefreshTokenValidity()
        );
    }
}
