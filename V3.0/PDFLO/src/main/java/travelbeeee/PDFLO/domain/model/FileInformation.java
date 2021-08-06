package travelbeeee.PDFLO.domain.model;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class FileInformation {
    private String originFileName;
    private String saltedFileName;
    private String location;

    public FileInformation(String originFileName, String saltedFileName, String location) {
        this.originFileName = originFileName;
        this.saltedFileName = saltedFileName;
        this.location = location;
    }

    public String fileReference(){
        return this.location + this.saltedFileName;
    }

    public String resizedFileReference(){
        return this.location + "Resized-" + this.saltedFileName;
    }
}
