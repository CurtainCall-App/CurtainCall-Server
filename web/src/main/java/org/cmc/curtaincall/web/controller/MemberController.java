package org.cmc.curtaincall.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.web.exception.EntityAccessDeniedException;
import org.cmc.curtaincall.web.security.annotation.LoginMemberId;
import org.cmc.curtaincall.web.service.account.AccountService;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.cmc.curtaincall.web.service.member.MemberService;
import org.cmc.curtaincall.web.service.member.request.MemberCreate;
import org.cmc.curtaincall.web.service.common.response.BooleanResult;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.member.request.MemberDelete;
import org.cmc.curtaincall.web.service.member.request.MemberEdit;
import org.cmc.curtaincall.web.service.member.response.MemberDetailResponse;
import org.cmc.curtaincall.web.service.party.response.PartyResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public MemberDetailResponse getMemberDetail(@PathVariable Long memberId) {
        return memberService.getDetail(memberId);
    }

    @PatchMapping("/member")
    public void editMember(
            @LoginMemberId Long memberId, @RequestBody @Validated MemberEdit memberEdit) {
        if (memberEdit.getImageId() != null && !imageService.isOwnedByMember(memberId, memberEdit.getImageId())) {
            throw new EntityAccessDeniedException(
                    "Member ID=" + memberId + ", Image ID=" + memberEdit.getImageId());
        }
        memberService.edit(memberId, memberEdit);
    }

    @GetMapping("/members/{memberId}/recruitments")
    public Slice<PartyResponse> getRecruitmentList(
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam PartyCategory category, @PathVariable Long memberId) {
        return memberService.getRecruitmentList(pageable, memberId, category);
    }

    @GetMapping("/members/{memberId}/participations")
    public Slice<PartyResponse> getParticipationList(
            Pageable pageable, @RequestParam PartyCategory category, @PathVariable Long memberId) {
        return memberService.getParticipationList(pageable, memberId, category);
    }

    @DeleteMapping("/member")
    public void delete(@LoginMemberId Long memberId, @RequestBody @Validated MemberDelete memberDelete) {
        accountService.withdraw(memberId);
    }
}
