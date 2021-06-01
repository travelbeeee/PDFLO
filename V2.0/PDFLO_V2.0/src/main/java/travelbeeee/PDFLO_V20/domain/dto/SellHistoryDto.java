package travelbeeee.PDFLO_V20.domain.dto;

import lombok.Data;
import travelbeeee.PDFLO_V20.domain.entity.OrderItem;
import java.time.format.DateTimeFormatter;

@Data
public class SellHistoryDto {
    private String username;
    private Integer orderPrice;
    private String createdDate;
    private String title;
    private Long orderId;

    public SellHistoryDto(OrderItem oi) {
        this.username = oi.getOrder().getMember().getUsername();
        this.orderPrice = oi.getItem().getPrice();
        this.createdDate = oi.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
        this.title = oi.getItem().getTitle();
        this.orderId = oi.getOrder().getId();
    }
}
