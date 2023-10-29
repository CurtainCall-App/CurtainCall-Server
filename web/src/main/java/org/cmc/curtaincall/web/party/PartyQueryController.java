package org.cmc.curtaincall.web.party;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.PartyCategory;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.dao.PartyDao;
import org.cmc.curtaincall.domain.party.request.PartySearchParam;
import org.cmc.curtaincall.domain.party.response.PartyDetailResponse;
import org.cmc.curtaincall.domain.party.response.PartyParticipationResponse;
import org.cmc.curtaincall.domain.party.response.PartyRecruitmentResponse;
import org.cmc.curtaincall.domain.party.response.PartyResponse;
import org.cmc.curtaincall.web.common.response.ListResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PartyQueryController {

    private final PartyDao partyDao;

    @GetMapping("/parties")
    public ListResult<PartyResponse> getPartyList(
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam PartyCategory category) {
        return new ListResult<>(partyDao.getList(pageable, category));
    }

    @GetMapping("/search/party")
    public ListResult<PartyResponse> searchParty(
            Pageable pageable, @ModelAttribute @Validated PartySearchParam partySearchParam) {
        return new ListResult<>(partyDao.search(pageable, partySearchParam));
    }

    @GetMapping("/parties/{partyId}")
    public PartyDetailResponse getPartyDetail(@PathVariable Long partyId) {
        return partyDao.getDetail(new PartyId(partyId));
    }

    @GetMapping("/members/{memberId}/recruitments")
    public ListResult<PartyRecruitmentResponse> getRecruitmentList(
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) PartyCategory category, @PathVariable Long memberId
    ) {
        return new ListResult<>(partyDao.getRecruitmentList(pageable, new MemberId(memberId), category));
    }

    @GetMapping("/members/{memberId}/participations")
    public ListResult<PartyParticipationResponse> getParticipationList(
            Pageable pageable,
            @RequestParam(required = false) PartyCategory category, @PathVariable Long memberId
    ) {
        return new ListResult<>(partyDao.getParticipationList(pageable, new MemberId(memberId), category));
    }
}
