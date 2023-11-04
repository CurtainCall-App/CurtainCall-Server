package org.cmc.curtaincall.domain.lostitem.infra;

import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.cmc.curtaincall.domain.lostitem.dao.LostItemExistsDao;
import org.cmc.curtaincall.domain.lostitem.exception.LostItemAccessDeniedException;
import org.cmc.curtaincall.domain.member.MemberId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LostItemCreatorValidatorImplTest {

    @InjectMocks
    private LostItemCreatorValidatorImpl lostItemCreatorValidator;

    @Mock
    private LostItemExistsDao lostItemExistsDao;

    @Test
    void validate() {
        // given
        given(lostItemExistsDao.existsByIdAndCreatedBy(new LostItemId(10L), new CreatorId(new MemberId(20L))))
                .willReturn(true);

        // expected
        assertThatCode(() -> lostItemCreatorValidator.validate(new LostItemId(10L), new CreatorId(new MemberId(20L))))
                .doesNotThrowAnyException();
    }

    @Test
    void validate_Fail() {
        // given
        given(lostItemExistsDao.existsByIdAndCreatedBy(new LostItemId(10L), new CreatorId(new MemberId(20L))))
                .willReturn(false);

        // expected
        assertThatThrownBy(() -> lostItemCreatorValidator.validate(new LostItemId(10L), new CreatorId(new MemberId(20L))))
                .isInstanceOf(LostItemAccessDeniedException.class);
    }
}