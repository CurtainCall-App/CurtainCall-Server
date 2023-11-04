package org.cmc.curtaincall.domain.review.infra;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.review.validation.ShowReviewShowValidator;
import org.cmc.curtaincall.domain.show.ShowId;
import org.cmc.curtaincall.domain.show.dao.ShowExistsDao;
import org.cmc.curtaincall.domain.show.exception.ShowNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShowReviewShowValidatorImpl implements ShowReviewShowValidator {

    private final ShowExistsDao showExistsDao;

    @Override
    public void validate(final ShowId showId) {
        if (!showExistsDao.exists(showId)) {
            throw new ShowNotFoundException(showId);
        }
    }
}
