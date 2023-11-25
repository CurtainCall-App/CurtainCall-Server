package org.cmc.curtaincall.web.member;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.member.request.MemberWithdraw;
import org.cmc.curtaincall.web.security.config.LoginMemberId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberWithdrawController {

    private final MemberWithdrawService withdrawService;

    @DeleteMapping("/member")
    public void withdraw(@LoginMemberId MemberId memberId, @RequestBody @Validated MemberWithdraw memberWithdraw) {
        withdrawService.withdraw(memberId, memberWithdraw);
    }
}
