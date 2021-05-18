package travelbeeee.PDFLO_V20.domain;

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
