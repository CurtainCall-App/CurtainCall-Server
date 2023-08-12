package org.cmc.curtaincall.web.service.lostitem;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.image.repository.ImageRepository;
import org.cmc.curtaincall.domain.lostitem.LostItem;
import org.cmc.curtaincall.domain.lostitem.repository.LostItemQueryRepository;
import org.cmc.curtaincall.domain.lostitem.repository.LostItemRepository;
import org.cmc.curtaincall.domain.lostitem.request.LostItemQueryParam;
import org.cmc.curtaincall.domain.show.Facility;
import org.cmc.curtaincall.domain.show.repository.FacilityRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.lostitem.request.LostItemCreate;
import org.cmc.curtaincall.web.service.lostitem.response.LostItemResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LostItemService {

    private final LostItemRepository lostItemRepository;

    private final LostItemQueryRepository lostItemQueryRepository;

    private final FacilityRepository facilityRepository;

    private final ImageRepository imageRepository;

    public IdResult<Long> create(LostItemCreate lostItemCreate) {
        Facility facility = getFacilityById(lostItemCreate.getFacilityId());
        Image image = getImageById(lostItemCreate.getImageId());
        LostItem lostItem = lostItemRepository.save(LostItem.builder()
                .facility(facility)
                .image(image)
                .title(lostItemCreate.getTitle())
                .type(lostItemCreate.getType())
                .foundPlaceDetail(lostItemCreate.getFoundPlaceDetail())
                .foundAt(lostItemCreate.getFoundAt())
                .particulars(lostItemCreate.getParticulars())
                .build()
        );
        return new IdResult<>(lostItem.getId());
    }

    public Slice<LostItemResponse> search(Pageable pageable, LostItemQueryParam queryParam) {
        return lostItemQueryRepository.query(pageable, queryParam)
                .map(lostItem -> LostItemResponse.builder()
                        .id(lostItem.getId())
                        .facilityId(lostItem.getFacility().getId())
                        .facilityName(lostItem.getFacility().getName())
                        .title(lostItem.getTitle())
                        .foundAt(lostItem.getFoundAt())
                        .imageUrl(lostItem.getImage().getUrl())
                        .build()
                );
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
}
