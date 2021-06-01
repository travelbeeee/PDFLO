package travelbeeee.PDFLO_V20.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import travelbeeee.PDFLO_V20.domain.entity.Order;
import travelbeeee.PDFLO_V20.domain.entity.OrderItem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
