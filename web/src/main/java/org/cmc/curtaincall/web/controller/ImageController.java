package org.cmc.curtaincall.web.controller;

import lombok.RequiredArgsConstructor;
import org.cmc.curtaincall.domain.core.CreatorId;
import org.cmc.curtaincall.domain.member.MemberId;
import org.cmc.curtaincall.web.exception.InvalidImageException;
import org.cmc.curtaincall.web.common.response.IdResult;
import org.cmc.curtaincall.web.security.config.LoginMemberId;
import org.cmc.curtaincall.web.service.image.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/images")
    public IdResult<Long> saveImage(@RequestPart MultipartFile image, @LoginMemberId MemberId memberId) {
        validateMultipartFileImage(image);
        Resource imageResource = image.getResource();
        return imageService.saveImage(imageResource, new CreatorId(memberId));
    }

    private void validateMultipartFileImage(MultipartFile image) {
        boolean valid = Optional.of(image)
                .filter(multipartFile -> !multipartFile.isEmpty())
                .filter(multipartFile -> Optional.ofNullable(multipartFile.getContentType())
                        .map(contentType -> contentType.startsWith("image/"))
                        .isPresent()
                )
                .isPresent();
        if (!valid) {
            throw new InvalidImageException(String.format(
                    "isEmpty=%s, contentType=%s", image.isEmpty(), image.getContentType()));
        }
    }
}
