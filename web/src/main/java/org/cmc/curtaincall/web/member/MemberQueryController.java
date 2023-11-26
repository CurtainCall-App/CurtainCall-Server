package org.cmc.curtaincall.web.member;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.member.dao.MemberDao;
import org.cmc.curtaincall.domain.member.response.MemberDetailResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberQueryController {

    private final MemberDao memberDao;

    @GetMapping("/members/{memberId}")
    public MemberDetailResponse getDetail(@PathVariable MemberId memberId) {
        return memberDao.getDetail(memberId);
    }
}
