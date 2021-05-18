package travelbeeee.PDFLOpjt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @ToString
public class Order {
    int userId;
    int contentId;
    int orderId;
    LocalDate localdate;
}
