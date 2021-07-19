package travelbeeee.PDFLO.domain.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import travelbeeee.PDFLO.domain.model.BaseEntity;
import travelbeeee.PDFLO.domain.model.FileInformation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Pdf extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "pdf_id")
    private Long id;

    private FileInformation fileInfo;

    public Pdf(FileInformation fileInfo) {
        this.fileInfo = fileInfo;
    }
}
