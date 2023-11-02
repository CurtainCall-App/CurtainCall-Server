package org.cmc.curtaincall.web.lostitem;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.image.repository.ImageRepository;
import org.cmc.curtaincall.domain.lostitem.LostItem;
import org.cmc.curtaincall.domain.lostitem.LostItemEditor;
import org.cmc.curtaincall.domain.lostitem.repository.LostItemRepository;
import org.cmc.curtaincall.domain.lostitem.validation.LostItemFacilityValidator;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.domain.show.Facility;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.domain.show.repository.FacilityRepository;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.lostitem.request.LostItemCreate;
import org.cmc.curtaincall.web.lostitem.request.LostItemEdit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LostItemService {

    private final LostItemRepository lostItemRepository;

    private final FacilityRepository facilityRepository;

    private final ImageRepository imageRepository;

    private final MemberRepository memberRepository;

    private final LostItemFacilityValidator lostItemFacilityValidator;

    @Transactional
    public IdResult<Long> create(LostItemCreate lostItemCreate) {
        FacilityId facilityId = new FacilityId(lostItemCreate.getFacilityId());
        lostItemFacilityValidator.validate(facilityId);
        Image image = getImageById(lostItemCreate.getImageId());
        LostItem lostItem = lostItemRepository.save(LostItem.builder()
                .facilityId(facilityId)
                .image(image)
                .title(lostItemCreate.getTitle())
                .type(lostItemCreate.getType())
                .foundPlaceDetail(lostItemCreate.getFoundPlaceDetail())
                .foundDate(lostItemCreate.getFoundDate())
                .foundTime(lostItemCreate.getFoundTime())
                .particulars(lostItemCreate.getParticulars())
                .build()
        );
        return new IdResult<>(lostItem.getId());
    }

    @Transactional
    public void delete(Long id) {
        LostItem lostItem = getLostItemById(id);
        lostItemRepository.delete(lostItem);
        imageRepository.delete(lostItem.getImage());
    }

    @Transactional
    public void edit(Long id, LostItemEdit lostItemEdit) {
        LostItem lostItem = getLostItemById(id);

        LostItemEditor editor = lostItem.toEditor()
                .image(getImageById(lostItemEdit.getImageId()))
                .title(lostItemEdit.getTitle())
                .type(lostItemEdit.getType())
                .foundPlaceDetail(lostItemEdit.getFoundPlaceDetail())
                .foundDate(lostItemEdit.getFoundDate())
                .foundTime(lostItemEdit.getFoundTime())
                .particulars(lostItem.getParticulars())
                .build();

        lostItem.edit(editor);
    }

    public boolean isOwnedByMember(Long lostItemId, Long memberId) {
        LostItem lostItem = getLostItemById(lostItemId);
        return Objects.equals(lostItem.getCreatedBy().getId(), memberId);
    }

    private Facility getFacilityById(String id) {
        return facilityRepository.findById(id)
                .filter(Facility::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Facility id=" + id));
    }

    private Image getImageById(Long id) {
        return imageRepository.findById(id)
                .filter(Image::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Image id=" + id));
    }

    private LostItem getLostItemById(Long id) {
        return lostItemRepository.findById(id)
                .filter(LostItem::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("LostItem id=" + id));
    }
}
