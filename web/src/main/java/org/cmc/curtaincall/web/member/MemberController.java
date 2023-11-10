package org.cmc.curtaincall.web.member;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.exception.EntityAccessDeniedException;
import org.cmc.curtaincall.web.member.request.MemberDelete;
import org.cmc.curtaincall.web.member.request.MemberEdit;
import org.cmc.curtaincall.web.security.LoginMemberId;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ImageService imageService;

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
    }
}
