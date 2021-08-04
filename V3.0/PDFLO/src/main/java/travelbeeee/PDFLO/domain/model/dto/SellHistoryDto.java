package travelbeeee.PDFLO.domain.model.dto;

import lombok.Data;
import travelbeeee.PDFLO.domain.model.entity.OrderItem;

import java.time.format.DateTimeFormatter;

@Data
public class SellHistoryDto {
    private String username;
    private Integer orderPrice;
    private String createdDate;
    private Long orderId;

    public SellHistoryDto(OrderItem oi) {
        this.username = oi.getOrder().getMember().getUsername();
        this.orderPrice = oi.getItem().getPrice();
        this.createdDate = oi.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
        this.orderId = oi.getOrder().getId();
    }
}
