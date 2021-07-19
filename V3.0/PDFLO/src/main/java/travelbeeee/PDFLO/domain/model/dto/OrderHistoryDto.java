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
public class OrderHistoryDto {
    private Long orderId;
    private String createdDate;
    private String title;
    private Integer price;
    private Long itemId;

    public OrderHistoryDto(Order order, OrderItem orderItem) {
        this.orderId = order.getId();
        this.createdDate = order.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
        this.title = orderItem.getItem().getTitle();
        this.price = orderItem.getItem().getPrice();
        this.itemId = orderItem.getItem().getId();
    }
}
