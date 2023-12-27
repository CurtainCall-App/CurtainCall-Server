package org.cmc.curtaincall.web.show.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.kopis")
public class KopisProperties {

    @NotNull
    private String serviceKey;

    @NotNull
    private String baseUrl;
}
