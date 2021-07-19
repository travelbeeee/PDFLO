package travelbeeee.PDFLO.domain.model;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
public class FileInformation {
    private String saltedFileName;
    private String location;
    private String extension;
}
