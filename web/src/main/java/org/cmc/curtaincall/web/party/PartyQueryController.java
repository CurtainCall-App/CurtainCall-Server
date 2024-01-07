package org.cmc.curtaincall.web.party;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.dao.PartyDao;
import org.cmc.curtaincall.domain.party.request.PartyListParam;
import org.cmc.curtaincall.domain.party.request.PartySearchParam;
import org.cmc.curtaincall.domain.party.response.PartyDetailResponse;
import org.cmc.curtaincall.domain.party.response.PartyParticipatedResponse;
import org.cmc.curtaincall.domain.party.response.PartyParticipationResponse;
import org.cmc.curtaincall.domain.party.response.PartyRecruitmentResponse;
import org.cmc.curtaincall.domain.party.response.PartyResponse;
import org.cmc.curtaincall.web.common.response.ListResult;
import org.cmc.curtaincall.web.security.config.LoginMemberId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PartyQueryController {

    private final PartyDao partyDao;

    @GetMapping("/parties")
    public ListResult<PartyResponse> getPartyList(
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable,
            @ModelAttribute final PartyListParam param
    ) {
        return new ListResult<>(partyDao.getList(pageable, param));
    }

    @GetMapping("/search/party")
    public ListResult<PartyResponse> searchParty(
            Pageable pageable, @ModelAttribute @Validated PartySearchParam partySearchParam) {
        return new ListResult<>(partyDao.search(pageable, partySearchParam));
    }

    @GetMapping("/parties/{partyId}")
    public PartyDetailResponse getPartyDetail(@PathVariable PartyId partyId) {
        return partyDao.getDetail(partyId);
    }

    @GetMapping("/members/{memberId}/recruitments")
    public ListResult<PartyRecruitmentResponse> getRecruitmentList(
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable,
            @PathVariable final MemberId memberId
    ) {
        return new ListResult<>(partyDao.getRecruitmentList(pageable, memberId));
    }

    @GetMapping("/members/{memberId}/participations")
    public ListResult<PartyParticipationResponse> getParticipationList(
            final Pageable pageable,
            @PathVariable final MemberId memberId
    ) {
        return new ListResult<>(partyDao.getParticipationList(pageable, memberId));
    }

    @GetMapping("/member/participated")
    public ListResult<PartyParticipatedResponse> getParticipated(
            @RequestParam @Validated @Size(max = 100) List<PartyId> partyIds, @LoginMemberId MemberId memberId
    ) {
        List<PartyParticipatedResponse> partyParticipatedResponses = partyDao.areParticipated(
                partyIds, memberId);
        return new ListResult<>(partyParticipatedResponses);
    }
}
