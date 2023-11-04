package org.cmc.curtaincall.web.common.config;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.dao.AccountDao;
import org.cmc.curtaincall.domain.lostitem.convert.StringToLostItemIdConverter;
import org.cmc.curtaincall.domain.member.convert.StringToMemberIdConverter;
import org.cmc.curtaincall.domain.party.convert.StringToPartyIdConverter;
import org.cmc.curtaincall.domain.review.convert.StringToShowReviewIdConverter;
import org.cmc.curtaincall.domain.show.convert.StringToFacilityIdConverter;
import org.cmc.curtaincall.domain.show.convert.StringToShowIdConverter;
import org.cmc.curtaincall.web.security.LoginMemberIdArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AccountDao accountDao;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberIdArgumentResolver(accountDao));
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToShowReviewIdConverter());
        registry.addConverter(new StringToPartyIdConverter());
        registry.addConverter(new StringToShowIdConverter());
        registry.addConverter(new StringToMemberIdConverter());
        registry.addConverter(new StringToLostItemIdConverter());
        registry.addConverter(new StringToFacilityIdConverter());
    }
}
