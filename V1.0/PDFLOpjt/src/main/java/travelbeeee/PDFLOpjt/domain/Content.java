package travelbeeee.PDFLOpjt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @ToString
public class Content {
    int userId;
    int pdfId;
    int thumbnailId;
    int contentId;
    String title;
    int price;
    String content;
    LocalDate localdate;
}
