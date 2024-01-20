package org.cmc.curtaincall.web.party;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.core.OptimisticLock;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.party.Party;
import org.cmc.curtaincall.domain.party.PartyEditor;
import org.cmc.curtaincall.domain.party.PartyHelper;
import org.cmc.curtaincall.domain.party.PartyId;
import org.cmc.curtaincall.domain.party.repository.PartyRepository;
import org.cmc.curtaincall.domain.party.validation.PartyMemberIdValidator;
import org.cmc.curtaincall.domain.party.validation.PartyShowIdValidator;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.web.party.request.PartyCreate;
import org.cmc.curtaincall.web.party.request.PartyEdit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyService {

    private final PartyRepository partyRepository;

    private final PartyMemberIdValidator memberIdValidator;

    private final PartyShowIdValidator showIdValidator;

    @Transactional
    public PartyId create(final PartyCreate partyCreate, final CreatorId createdBy) {
        ShowId showId = partyCreate.getShowId();
        showIdValidator.validate(showId);
        Party party = partyRepository.save(Party.builder()
                .showId(showId)
                .partyAt(partyCreate.getShowAt())
                .title(partyCreate.getTitle())
                .content(partyCreate.getContent())
                .maxMemberNum(partyCreate.getMaxMemberNum())
                .createdBy(createdBy)
                .build()
        );

        return new PartyId(party.getId());
    }

    @Transactional
    @OptimisticLock
    public void participate(final PartyId partyId, final MemberId memberId) {
        memberIdValidator.validate(memberId);
        Party party = PartyHelper.get(partyId, partyRepository);
        party.participate(memberId);
    }

    @Transactional
    public void edit(PartyId id, PartyEdit partyEdit) {
        Party party = PartyHelper.get(id, partyRepository);

        PartyEditor editor = party.toEditor()
                .title(partyEdit.getTitle())
                .content(partyEdit.getContent())
                .build();

        party.edit(editor);
    }

}
