package org.cmc.curtaincall.web.security;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
@Data
public class CurtainCallJwtProperties {

    @NotNull
    private String secret;

    @NotNull
    private long accessTokenValidity;
}
