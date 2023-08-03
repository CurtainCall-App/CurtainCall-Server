package org.cmc.curtaincall.domain.image;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cmc.curtaincall.domain.core.BaseEntity;

@Entity
@Table(name = "images")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "images_id")
    private Long id;

    @Column(name = "origin_name", nullable = false)
    private String originName;

    @Column(name = "stored_name", nullable = false)
    private String storedName;

    @Column(name = "url", nullable = false)
    private String url;

    @Builder
    private Image(
            String originName,
            String storedName,
            String url) {
        this.originName = originName;
        this.storedName = storedName;
        this.url = url;
    }
}
