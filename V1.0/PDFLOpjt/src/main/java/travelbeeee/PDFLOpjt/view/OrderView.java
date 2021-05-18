package travelbeeee.PDFLOpjt.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @ToString
public class OrderView {
    // 어떤 제목의 content를 언제 구매했는지.
    int contentId;
    String title;
    LocalDate localdate;
}
