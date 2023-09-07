package org.cmc.curtaincall.web.service.lostitem;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.image.repository.ImageRepository;
import org.cmc.curtaincall.domain.lostitem.LostItem;
import org.cmc.curtaincall.domain.lostitem.LostItemEditor;
import org.cmc.curtaincall.domain.lostitem.repository.LostItemQueryRepository;
import org.cmc.curtaincall.domain.lostitem.repository.LostItemRepository;
import org.cmc.curtaincall.domain.lostitem.request.LostItemQueryParam;
import org.cmc.curtaincall.domain.member.Member;
import org.cmc.curtaincall.domain.member.repository.MemberRepository;
import org.cmc.curtaincall.domain.show.Facility;
import org.cmc.curtaincall.domain.show.repository.FacilityRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.lostitem.request.LostItemCreate;
import org.cmc.curtaincall.web.service.lostitem.request.LostItemEdit;
import org.cmc.curtaincall.web.service.lostitem.response.LostItemDetailResponse;
import org.cmc.curtaincall.web.service.lostitem.response.LostItemMyResponse;
import org.cmc.curtaincall.web.service.lostitem.response.LostItemResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LostItemService {

    private final LostItemRepository lostItemRepository;
    private final LostItemQueryRepository lostItemQueryRepository;
    private final FacilityRepository facilityRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public IdResult<Long> create(LostItemCreate lostItemCreate) {
        Facility facility = getFacilityById(lostItemCreate.getFacilityId());
        Image image = getImageById(lostItemCreate.getImageId());
        LostItem lostItem = lostItemRepository.save(LostItem.builder()
                .facility(facility)
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

    public Slice<LostItemResponse> search(Pageable pageable, LostItemQueryParam queryParam) {
        return lostItemQueryRepository.query(pageable, queryParam)
                .map(LostItemResponse::of);
    }

    public LostItemDetailResponse getDetail(Long id) {
        LostItem lostItem = getLostItemById(id);
        return LostItemDetailResponse.builder()
                .id(lostItem.getId())
                .facilityId(lostItem.getFacility().getId())
                .facilityName(lostItem.getFacility().getName())
                .facilityPhone(lostItem.getFacility().getPhone())
                .title(lostItem.getTitle())
                .type(lostItem.getType())
                .foundPlaceDetail(lostItem.getFoundPlaceDetail())
                .foundDate(lostItem.getFoundDate())
                .foundTime(lostItem.getFoundTime())
                .particulars(lostItem.getParticulars())
                .imageId(lostItem.getImage().getId())
                .imageUrl(lostItem.getImage().getUrl())
                .createdAt(lostItem.getCreatedAt())
                .build();
    }

    public Slice<LostItemMyResponse> getMyList(Pageable pageable, Long memberId) {
        Member member = memberRepository.getReferenceById(memberId);
        return lostItemRepository.findSliceByCreatedByAndUseYnIsTrue(pageable, member)
                .map(LostItemMyResponse::of);
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
