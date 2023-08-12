package org.cmc.curtaincall.web.service.image;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.image.repository.ImageRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.common.response.IdResult;
import org.cmc.curtaincall.web.service.image.store.ImageStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

    private final ImageStore imageStore;

    private final ImageRepository imageRepository;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    @Transactional
    public IdResult<Long> saveImage(Resource imageResource) {
        String originName = imageResource.getFilename();
        String storedName = createStoredName(originName);
        String url = imageStore.store(imageResource, storedName);
        Image image = imageRepository.save(
                Image.builder()
                        .originName(originName)
                        .storedName(storedName)
                        .url(url)
                        .build()
        );

        return new IdResult<>(image.getId());
    }

    private String createStoredName(String originalName) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalName);
        String now = LocalDateTime.now().format(dateTimeFormatter);
        return now + "/" + uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public boolean isOwnedByMember(Long memberId, Long imageId) {
        Image image = getImageById(imageId);
        return Objects.equals(image.getCreatedBy().getId(), memberId);
    }

    private Image getImageById(Long imageId) {
        return imageRepository.findById(imageId)
                .filter(Image::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Image.id=" + imageId));
    }

}
