package org.cmc.curtaincall.web.service.image.store;

import org.cmc.curtaincall.web.exception.InvalidImageException;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@Profile("local")
public class LocalImageStore implements ImageStore {

    public static final File IMAGE_DIR = new File(System.getProperty("user.dir"), "localimage");

    @Override
    public String store(Resource image, String storedName) {
        File imagePath = new File(IMAGE_DIR, storedName);
        File imageDir = imagePath.getParentFile();
        if (!imageDir.exists() && !imageDir.mkdirs()) {
            throw new IllegalStateException("이미지 저장을 위한 로컬 디렉토리 생성에 실패했습니다.");
        }

        try (InputStream in = image.getInputStream(); OutputStream out = new FileOutputStream(imagePath)) {
            out.write(in.readAllBytes());
            return imagePath.toString();
        } catch (IOException e) {
            throw new InvalidImageException(e);
        }
    }
}
