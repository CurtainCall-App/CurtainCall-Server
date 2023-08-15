package org.cmc.curtaincall.web.service.member;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.domain.party.repository.PartyMemberRepository;
import org.cmc.curtaincall.domain.party.repository.PartyRepository;
import org.cmc.curtaincall.web.exception.AlreadyNicknameExistsException;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.common.response.BooleanResult;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.member.request.MemberCreate;
import org.cmc.curtaincall.web.service.member.response.MemberDetailResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final PartyRepository partyRepository;

    private final PartyMemberRepository partyMemberRepository;

    public BooleanResult checkNicknameDuplicate(String nickname) {
        return new BooleanResult(memberRepository.existsByNickname(nickname));
    }

    @Transactional
    public IdResult<Long> create(MemberCreate memberCreate) {
        String nickname = memberCreate.getNickname();
        if (memberRepository.existsByNickname(nickname)) {
            throw new AlreadyNicknameExistsException("nickname=" + nickname);
        }
        Member member = memberRepository.save(Member.builder()
                .nickname(nickname)
                .build());
        return new IdResult<>(member.getId());
    }

    public MemberDetailResponse getDetail(Long memberId) {
        Member member = getMemberById(memberId);
        long recruitingNum = partyRepository.countByCreatedByAndUseYnIsTrue(member);
        long participationNum = partyMemberRepository.countByMember(member);
        return MemberDetailResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .imageUrl(getImageUrlOf(member))
                .recruitingNum(recruitingNum)
                .participationNum(participationNum)
                .build();
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .filter(Member::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Member id=" + id));
    }

    private String getImageUrlOf(Member member) {
        return Optional.ofNullable(member.getImage())
                .filter(Image::getUseYn)
                .map(Image::getUrl)
                .orElse(null);
    }
}
