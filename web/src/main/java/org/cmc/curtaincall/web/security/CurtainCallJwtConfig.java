package org.cmc.curtaincall.web.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
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
    public CurtainCallJwtEncoderService curtainCallJwtEncoderService(
            CurtainCallJwtProperties properties, JwtEncoder curtainCallJwtEncoder) {
        return new CurtainCallJwtEncoderService(curtainCallJwtEncoder, properties.getAccessTokenValidity());
    }

    @Bean
    public UsernameService usernameService(OAuth2ClientProperties properties) {
        return new UsernameService(properties);
    }
}
