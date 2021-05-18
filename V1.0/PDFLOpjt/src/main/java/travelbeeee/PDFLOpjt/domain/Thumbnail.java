package travelbeeee.PDFLOpjt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Thumbnail {
    int contentId;
    int thumbnailId;
    String originFileName;
    String saltedFileName;
    String location;
}
