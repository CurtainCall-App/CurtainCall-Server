package org.cmc.curtaincall.web.service.image;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.image.Image;
import org.cmc.curtaincall.domain.image.repository.ImageRepository;
import org.cmc.curtaincall.web.exception.EntityNotFoundException;
import org.cmc.curtaincall.web.service.image.store.ImageStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageDeleteService {

    private final ImageRepository imageRepository;

    private final ImageStore imageStore;

    @Transactional
    public void delete(long id) {
        Image image = getImageById(id);
        imageStore.delete(image.getStoredName());
        imageRepository.delete(image);
    }

    private Image getImageById(long id) {
        return imageRepository.findById(id)
                .filter(Image::getUseYn)
                .orElseThrow(() -> new EntityNotFoundException("Image.id=" + id));
    }
}
