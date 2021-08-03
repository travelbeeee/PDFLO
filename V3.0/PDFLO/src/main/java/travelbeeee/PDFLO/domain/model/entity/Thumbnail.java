package travelbeeee.PDFLO.domain.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import travelbeeee.PDFLO.domain.model.BaseEntity;
import travelbeeee.PDFLO.domain.model.FileInformation;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Thumbnail extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "thumbnail_id")
    private Long id;

    @Embedded
    private FileInformation fileInfo;

    public Thumbnail(FileInformation fileInfo) {
        this.fileInfo = fileInfo;
    }

    public void changeThumbnail(FileInformation fileInfo) {
        this.fileInfo = fileInfo;
    }
}
