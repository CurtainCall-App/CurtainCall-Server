package org.cmc.curtaincall.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.web.service.account.AccountService;
import org.cmc.curtaincall.web.service.member.MemberService;
import org.cmc.curtaincall.web.service.member.request.MemberCreate;
import org.cmc.curtaincall.web.service.common.response.BooleanResult;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.member.response.MemberDetailResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final AccountService accountService;

    @GetMapping("/members/duplicate/nickname")
    public BooleanResult getNicknameDuplicate(@RequestParam @NotBlank @Size(max = 15) String nickname) {
        return memberService.checkNicknameDuplicate(nickname);
    }

    @PostMapping("/signup")
    public IdResult<Long> signup(
            @Valid @RequestBody MemberCreate memberCreate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        IdResult<Long> memberCreateResult = memberService.create(memberCreate);
        accountService.signupMember(username, memberCreateResult.getId());
        return memberCreateResult;
    }

    @GetMapping("/members/{memberId}")
    public MemberDetailResponse getMemberDetail(@PathVariable Long memberId) {
        return memberService.getDetail(memberId);
    }
}
