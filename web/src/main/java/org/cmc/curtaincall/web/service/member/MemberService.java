package org.cmc.curtaincall.web.service.member;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.image.repository.ImageRepository;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.MemberEditor;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.domain.party.repository.PartyMemberRepository;
import org.cmc.curtaincall.domain.party.repository.PartyRepository;
import org.cmc.curtaincall.web.exception.AlreadyNicknameExistsException;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.common.response.BooleanResult;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.member.request.MemberCreate;
import org.cmc.curtaincall.web.service.member.request.MemberEdit;
import org.cmc.curtaincall.web.service.member.response.MemberDetailResponse;
import org.cmc.curtaincall.web.service.party.response.PartyResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final PartyRepository partyRepository;

    private final PartyMemberRepository partyMemberRepository;

    private final ImageRepository imageRepository;

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

    @Transactional
    public void edit(Long memberId, MemberEdit memberEdit) {
        Member member = getMemberById(memberId);
        MemberEditor.MemberEditorBuilder editorBuilder = member.toEditor()
                .nickname(memberEdit.getNickname());
        if (!isImageIdEqual(member.getImage(), memberEdit.getImageId())) {
            Optional.ofNullable(member.getImage())
                    .ifPresent(Image::delete);
            Image imageToEdit = Optional.ofNullable(memberEdit.getImageId())
                    .map(this::getImageById)
                    .orElse(null);
            editorBuilder.image(imageToEdit);
        }

        member.edit(editorBuilder.build());
    }

    public Slice<PartyResponse> getRecruitmentList(Pageable pageable, Long memberId, PartyCategory category) {
        Member member = memberRepository.getReferenceById(memberId);
        return partyRepository.findSliceWithByCreatedByAndCategoryAndUseYnIsTrue(pageable, member, category)
                .map(party -> PartyResponse.builder()
                        .id(party.getId())
                        .title(party.getTitle())
                        .curMemberNum(party.getCurMemberNum())
                        .maxMemberNum(party.getMaxMemberNum())
                        .showAt(party.getShowAt())
                        .createdAt(party.getCreatedAt())
                        .category(party.getCategory())
                        .creatorId(party.getCreatedBy().getId())
                        .creatorNickname(party.getCreatedBy().getNickname())
                        .creatorImageUrl(getImageUrlOf(party.getCreatedBy()))
                        .showId(party.getShow().getId())
                        .showName(party.getShow().getName())
                        .showPoster(party.getShow().getPoster())
                        .facilityId(party.getShow().getFacility().getId())
                        .facilityName(party.getShow().getFacility().getName())
                        .build()
                );
    }

    private boolean isImageIdEqual(@Nullable Image image, @Nullable Long imageId) {
        Long id = Optional.ofNullable(image)
                .map(Image::getId)
                .orElse(null);
        return Objects.equals(id, imageId);
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .filter(Member::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Member id=" + id));
    }

    private Image getImageById(Long id) {
        return imageRepository.findById(id)
                .filter(Image::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Image id=" + id));
    }

    private String getImageUrlOf(Member member) {
        return Optional.ofNullable(member.getImage())
                .filter(Image::getUseYn)
                .map(Image::getUrl)
                .orElse(null);
    }
}
