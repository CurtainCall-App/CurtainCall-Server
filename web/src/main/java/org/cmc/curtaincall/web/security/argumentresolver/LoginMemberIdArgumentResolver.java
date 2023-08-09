package org.cmc.curtaincall.web.security.argumentresolver;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.security.annotation.LoginMemberId;
import org.cmc.curtaincall.web.service.account.AccountService;
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

    private final AccountService accountService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasCurrentUsernameAnnotation = parameter.hasParameterAnnotation(LoginMemberId.class);
        boolean hasStringType = (Long.class == parameter.getParameterType());
        return hasCurrentUsernameAnnotation && hasStringType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName)
                .map(accountService::getMemberId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "로그인되지 않은 회원이거나 존재하지 않는 회원입니다." + authentication
                ));
    }
}
