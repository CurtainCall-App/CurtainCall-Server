package org.cmc.curtaincall.web.service.party;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.domain.party.PartyEditor;
import org.cmc.curtaincall.domain.party.repository.PartyRepository;
import org.cmc.curtaincall.domain.show.Show;
import org.cmc.curtaincall.domain.show.repository.ShowRepository;
import org.cmc.curtaincall.web.exception.AlreadyClosedPartyException;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.party.request.PartyCreate;
import org.cmc.curtaincall.web.service.party.request.PartyEdit;
import org.cmc.curtaincall.web.service.party.response.PartyDetailResponse;
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
public class PartyService {

    private final PartyRepository partyRepository;

    private final ShowRepository showRepository;

    private final MemberRepository memberRepository;

    public PartyDetailResponse getDetail(Long id) {
        Party party = getPartyById(id);
        return PartyDetailResponse.builder()
                .id(party.getId())
                .title(party.getTitle())
                .content(party.getContent())
                .curMemberNum(party.getCurMemberNum())
                .maxMemberNum(party.getMaxMemberNum())
                .showAt(party.getShowAt())
                .createdAt(party.getCreatedAt())
                .creatorId(party.getCreatedBy().getId())
                .creatorNickname(party.getCreatedBy().getNickname())
                .creatorImageUrl(getImageUrlOf(party.getCreatedBy()))
                .showId(party.getShow().getId())
                .showName(party.getShow().getName())
                .facilityId(party.getShow().getFacility().getId())
                .facilityName(party.getShow().getFacility().getName())
                .build();
    }

    public Slice<PartyResponse> getList(Pageable pageable, PartyCategory category) {
        return partyRepository.findSliceWithByCategoryAndUseYnIsTrueOrderByCreatedAtDesc(pageable, category)
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

    public IdResult<Long> create(PartyCreate partyCreate) {
        Show show = getShowById(partyCreate.getShowId());
        Party party = partyRepository.save(Party.builder()
                .show(show)
                .showAt(partyCreate.getShowAt())
                .title(partyCreate.getTitle())
                .content(partyCreate.getContent())
                .maxMemberNum(partyCreate.getMaxMemberNum())
                .category(partyCreate.getCategory())
                .build()
        );

        return new IdResult<>(party.getId());
    }

    @Transactional
    public void participate(Long partyId, Long memberId) {
        Party party = getPartyById(partyId);
        if (Boolean.FALSE.equals(party.getClosed())) {
            throw new AlreadyClosedPartyException("Party id=" + partyId);
        }

        if (isParticipated(memberId, party)) {
            return;
        }

        Member member = getMemberById(memberId);
        party.participate(member);
    }

    @Transactional
    public void edit(Long id, PartyEdit partyEdit) {
        Party party = getPartyById(id);

        PartyEditor editor = party.toEditor()
                .title(partyEdit.getTitle())
                .content(partyEdit.getContent())
                .build();

        party.edit(editor);
    }

    private boolean isParticipated(Long memberId, Party party) {
        return party.getPartyMembers().stream()
                .anyMatch(partyMember -> Objects.equals(partyMember.getMember().getId(), memberId))
                || Objects.equals(party.getCreatedBy().getId(), memberId);
    }

    public boolean isOwnedByMember(Long partyId, Long memberId) {
        Party party = getPartyById(partyId);
        return Objects.equals(party.getCreatedBy().getId(), memberId);
    }

    @Transactional
    public void delete(Long partyId) {
        Party party = getPartyById(partyId);
        party.getPartyMembers().clear();
        party.delete();
    }

    private Show getShowById(String id) {
        return showRepository.findById(id)
                .filter(Show::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Show id=" + id));
    }

    private Party getPartyById(Long id) {
        return partyRepository.findById(id)
                .filter(Party::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Party id=" + id));
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
