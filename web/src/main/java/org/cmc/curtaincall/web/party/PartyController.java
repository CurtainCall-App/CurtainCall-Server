package org.cmc.curtaincall.web.party;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.validation.PartyCreatorValidator;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.party.request.PartyCreate;
import org.cmc.curtaincall.web.party.request.PartyEdit;
import org.cmc.curtaincall.web.security.config.LoginMemberId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    private final PartyCreatorValidator partyCreatorValidator;

    @PostMapping("/parties")
    public IdResult<PartyId> createParty(
            @RequestBody @Validated PartyCreate partyCreate, @LoginMemberId MemberId memberId
    ) {
        return new IdResult<>(partyService.create(partyCreate, new CreatorId(memberId)));
    }

    @InitBinder("partyCreate")
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(new PartyCreateValidator());
    }

    @DeleteMapping("/parties/{partyId}")
    public void deleteParty(@PathVariable PartyId partyId, @LoginMemberId MemberId memberId) {
        partyCreatorValidator.validate(partyId, new CreatorId(memberId));
        partyService.delete(partyId);
    }

    @PatchMapping("/parties/{partyId}")
    public void editParty(
            @PathVariable PartyId partyId, @RequestBody @Validated PartyEdit partyEdit,
            @LoginMemberId MemberId memberId) {
        partyCreatorValidator.validate(partyId, new CreatorId(memberId));
        partyService.edit(partyId, partyEdit);
    }

    @PutMapping("/member/parties/{partyId}")
    public void participateParty(@PathVariable PartyId partyId, @LoginMemberId MemberId memberId) {
        partyService.participate(partyId, memberId);
    }

}
