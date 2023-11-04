package org.cmc.curtaincall.domain.lostitem.dao;

import org.cmc.curtaincall.domain.common.AbstractDataJpaTest;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.lostitem.LostItem;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.cmc.curtaincall.domain.lostitem.LostItemType;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@Import(LostItemExistsDao.class)
class LostItemExistsDaoTest extends AbstractDataJpaTest {

    @Autowired
    private LostItemExistsDao lostItemExistsDao;

    @Test
    void existsByIdAndCreatedBy() {
        // given
        given(auditorProvider.getCurrentAuditor()).willReturn(
                Optional.of(new CreatorId(new MemberId(20L))));

        LostItem lostItem = LostItem.builder()
                .facilityId(new FacilityId("facility-id"))
                .image(em.getReference(Image.class, 10L))
                .title("title")
                .type(LostItemType.BAG)
                .foundPlaceDetail("detail")
                .foundDate(LocalDate.of(2023, 11, 4))
                .foundTime(LocalTime.of(0, 37))
                .particulars("particulars")
                .build();
        em.persist(lostItem);

        em.flush();
        em.clear();

        // expected
        assertThat(lostItemExistsDao.existsByIdAndCreatedBy(
                new LostItemId(lostItem.getId()), new CreatorId(new MemberId(20L)))
        ).isTrue();
    }

    @Test
    void existsByIdAndCreatedBy_IdDifferent() {
        // given
        given(auditorProvider.getCurrentAuditor()).willReturn(
                Optional.of(new CreatorId(new MemberId(20L))));

        LostItem lostItem = LostItem.builder()
                .facilityId(new FacilityId("facility-id"))
                .image(em.getReference(Image.class, 10L))
                .title("title")
                .type(LostItemType.BAG)
                .foundPlaceDetail("detail")
                .foundDate(LocalDate.of(2023, 11, 4))
                .foundTime(LocalTime.of(0, 37))
                .particulars("particulars")
                .build();
        em.persist(lostItem);

        em.flush();
        em.clear();

        // expected
        assertThat(lostItemExistsDao.existsByIdAndCreatedBy(
                new LostItemId(lostItem.getId() + 1), new CreatorId(new MemberId(20L)))
        ).isFalse();
    }

    @Test
    void existsByIdAndCreatedBy_CreatedByDifferent() {
        // given
        given(auditorProvider.getCurrentAuditor()).willReturn(
                Optional.of(new CreatorId(new MemberId(20L))));

        LostItem lostItem = LostItem.builder()
                .facilityId(new FacilityId("facility-id"))
                .image(em.getReference(Image.class, 10L))
                .title("title")
                .type(LostItemType.BAG)
                .foundPlaceDetail("detail")
                .foundDate(LocalDate.of(2023, 11, 4))
                .foundTime(LocalTime.of(0, 37))
                .particulars("particulars")
                .build();
        em.persist(lostItem);

        em.flush();
        em.clear();

        // expected
        assertThat(lostItemExistsDao.existsByIdAndCreatedBy(
                new LostItemId(lostItem.getId()), new CreatorId(new MemberId(20L + 1)))
        ).isFalse();
    }
}