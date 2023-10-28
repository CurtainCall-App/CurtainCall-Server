package org.cmc.curtaincall.web.security;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.dao.AccountDao;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.security.LoginMemberId;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@RequiredArgsConstructor
public class LoginMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final AccountDao accountDao;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasCurrentUsernameAnnotation = parameter.hasParameterAnnotation(LoginMemberId.class);
        boolean hasStringType = (MemberId.class == parameter.getParameterType());
        return hasCurrentUsernameAnnotation && hasStringType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName)
                .flatMap(accountDao::findMemberIdByUsername)
                .orElse(null);
    }
}
