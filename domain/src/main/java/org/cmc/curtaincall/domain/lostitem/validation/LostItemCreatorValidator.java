package org.cmc.curtaincall.domain.lostitem.validation;

import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.lostitem.LostItemId;

public interface LostItemCreatorValidator {

    void validate(final LostItemId lostItemId, final CreatorId creatorId);
}
