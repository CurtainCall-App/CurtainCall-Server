package org.cmc.curtaincall.web.lostitem;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.image.repository.ImageRepository;
import org.cmc.curtaincall.domain.lostitem.LostItem;
import org.cmc.curtaincall.domain.lostitem.LostItemEditor;
import org.cmc.curtaincall.domain.lostitem.LostItemHelper;
import org.cmc.curtaincall.domain.lostitem.LostItemId;
import org.cmc.curtaincall.domain.lostitem.repository.LostItemRepository;
import org.cmc.curtaincall.domain.lostitem.validation.LostItemFacilityValidator;
import org.cmc.curtaincall.domain.show.FacilityId;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.lostitem.request.LostItemCreate;
import org.cmc.curtaincall.web.lostitem.request.LostItemEdit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LostItemService {

    private final LostItemRepository lostItemRepository;

    private final ImageRepository imageRepository;

    private final LostItemFacilityValidator lostItemFacilityValidator;

    @Transactional
    public IdResult<Long> create(final LostItemCreate lostItemCreate) {
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
    public void delete(final LostItemId id) {
        LostItem lostItem = LostItemHelper.get(id, lostItemRepository);
        lostItemRepository.delete(lostItem);
        imageRepository.delete(lostItem.getImage());
    }

    @Transactional
    public void edit(final LostItemId id, final LostItemEdit lostItemEdit) {
        LostItem lostItem = LostItemHelper.get(id, lostItemRepository);

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

    private Image getImageById(Long id) {
        return imageRepository.findById(id)
                .filter(Image::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Image id=" + id));
    }

}
