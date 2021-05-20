package travelbeeee.PDFLO_V20.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import travelbeeee.PDFLO_V20.domain.BaseEntity;
import travelbeeee.PDFLO_V20.domain.FileInformation;

import javax.persistence.*;

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
