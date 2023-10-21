package com.greamz.backend.model;

import com.greamz.backend.common.TimeStampEntity;
import com.greamz.backend.enumeration.FileStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "images")
public class Image extends TimeStampEntity {
    @Id
    private String id;
    private String title;
    @Column(length = 1000)
    private String link;
    private FileStatus status = FileStatus.Closed;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)

    private GameModel gameModel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Image image = (Image) o;
        return getId() != null && Objects.equals(getId(), image.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
