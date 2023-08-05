package org.cmc.curtaincall.domain.image.repository;

import org.cmc.curtaincall.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
