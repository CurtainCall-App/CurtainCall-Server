package org.cmc.curtaincall.web.security.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.account.dao.AccountDao;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.common.response.BooleanResult;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.security.request.SignupRequest;
import org.cmc.curtaincall.web.security.service.SignupService;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountDao accountDao;

    private final SignupService signupService;

    @GetMapping("/user/member-id")
    public IdResult<Long> getUserMemberId(Authentication authentication) {
        String username = authentication.getName();
        return new IdResult<>(accountDao.findMemberIdByUsername(username).map(MemberId::getId).orElse(null));
    }

    @GetMapping("/members/duplicate/nickname")
    public BooleanResult getNicknameDuplicate(@RequestParam @NotBlank @Size(max = 15) String nickname) {
        return new BooleanResult(signupService.checkNicknameDuplicate(nickname));
    }

    @PostMapping("/signup")
    public IdResult<MemberId> signup(
            @Validated @RequestBody final SignupRequest signupRequest,
            final Authentication authentication
    ) {
        final String username = authentication.getName();
        final MemberId memberId = signupService.signup(username, signupRequest);
        return new IdResult<>(memberId);
    }
}
