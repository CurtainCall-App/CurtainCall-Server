package org.cmc.curtaincall.web.config;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.security.argumentresolver.LoginMemberIdArgumentResolver;
import org.cmc.curtaincall.web.security.service.AccountService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AccountService accountService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberIdArgumentResolver(accountService));
    }
}
