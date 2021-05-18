package travelbeeee.PDFLO_V20.domain.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import travelbeeee.PDFLO_V20.domain.BaseEntity;
import travelbeeee.PDFLO_V20.domain.FileInformation;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pdf extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "pdf_id")
    private Long id;

    private FileInformation fileInfo;
}
