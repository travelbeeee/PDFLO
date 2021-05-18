package travelbeeee.PDFLOpjt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Pdf {
    int contentId;
    int pdfId;
    String originFileName;
    String saltedFileName;
    String location;
}
