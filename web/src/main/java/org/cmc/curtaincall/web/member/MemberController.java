package org.cmc.curtaincall.web.member;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.common.response.BooleanResult;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.exception.EntityAccessDeniedException;
import org.cmc.curtaincall.web.security.AccountService;
import org.cmc.curtaincall.web.security.LoginMemberId;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.cmc.curtaincall.web.member.MemberService;
import org.cmc.curtaincall.web.member.request.MemberCreate;
import org.cmc.curtaincall.web.member.request.MemberDelete;
import org.cmc.curtaincall.web.member.request.MemberEdit;
import org.cmc.curtaincall.web.member.response.MemberDetailResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AccountService accountService;
    private final ImageService imageService;

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
    public MemberDetailResponse getMemberDetail(@PathVariable MemberId memberId) {
        return memberService.getDetail(memberId.getId());
    }

    @PatchMapping("/member")
    public void editMember(
            @LoginMemberId MemberId memberId, @RequestBody @Validated MemberEdit memberEdit) {
        if (memberEdit.getImageId() != null && !imageService.isOwnedByMember(memberId.getId(), memberEdit.getImageId())) {
            throw new EntityAccessDeniedException(
                    "Member ID=" + memberId + ", Image ID=" + memberEdit.getImageId());
        }
        memberService.edit(memberId.getId(), memberEdit);
    }

    @DeleteMapping("/member")
    public void delete(@LoginMemberId MemberId memberId, @RequestBody @Validated MemberDelete memberDelete) {
        accountService.delete(new MemberId(memberId.getId()));
    }
}
