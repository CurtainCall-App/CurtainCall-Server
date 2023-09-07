package org.cmc.curtaincall.web.service.image.store;

import org.springframework.core.io.Resource;

public interface ImageStore {

    /**
     * @param image 저장할 이미지
     * @param storedName 저장할 이름
     * @return 저장된 URL, 경로
     */
    String store(Resource image, String storedName);

    void delete(String storedName);
}
