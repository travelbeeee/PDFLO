package travelbeeee.PDFLO.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import travelbeeee.PDFLO.domain.model.entity.Order;
import travelbeeee.PDFLO.domain.model.entity.OrderItem;

import java.time.format.DateTimeFormatter;

/**
 * 회원 주문 내역 Dto
 */
@Data
@AllArgsConstructor
public class OrderDto {
    private Long orderId;
    private String createdDate;
    private Integer totalPrice;
    private Integer orderCount;

    public OrderDto(Order o) {
        this.orderId = o.getId();
        this.createdDate = o.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
        this.totalPrice = o.getTotalPrice();
        this.orderCount = o.getOrderCount();
    }
}
