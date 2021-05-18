package travelbeeee.PDFLOpjt.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @ToString
public class SellView {
    // username이 localdate 때 title 을 구매함.
    String username;
    String title;
    LocalDate localdate;
}
