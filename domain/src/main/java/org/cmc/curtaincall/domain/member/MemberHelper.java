package org.cmc.curtaincall.domain.member;

import org.cmc.curtaincall.domain.member.exception.MemberNotFoundException;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;

public final class MemberHelper {

    private MemberHelper() {
        throw new UnsupportedOperationException();
    }

    public static Member get(MemberId id, MemberRepository memberRepository) {
        return memberRepository.findById(id.getId())
                .filter(Member::getUseYn)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

}
