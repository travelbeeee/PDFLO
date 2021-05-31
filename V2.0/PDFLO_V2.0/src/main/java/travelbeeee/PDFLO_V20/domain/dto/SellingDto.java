package travelbeeee.PDFLO_V20.domain.dto;

import lombok.Data;
import travelbeeee.PDFLO_V20.domain.entity.OrderItem;

import java.time.LocalDateTime;

@Data
public class SellingDto {
    private String username;
    private Integer orderPrice;
    private LocalDateTime createdDate;

    public SellingDto(OrderItem oi) {
        this.username = oi.getOrder().getMember().getUsername();
        this.orderPrice = oi.getOrderPrice();
        this.createdDate = oi.getCreatedDate();
    }
}
