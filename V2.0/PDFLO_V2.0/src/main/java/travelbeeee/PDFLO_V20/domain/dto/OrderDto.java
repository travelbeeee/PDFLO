package travelbeeee.PDFLO_V20.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import travelbeeee.PDFLO_V20.domain.entity.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class OrderDto {
    private Long orderId;
    private int totalPrice;
    private LocalDateTime createdDate;
    private List<ItemOrderDto> itemOrderDtos = new ArrayList<>();

    public OrderDto(Order o) {
        this.orderId = o.getId();
        this.totalPrice = o.getTotalPrice();
        this.createdDate = o.getCreatedDate();
        this.itemOrderDtos = o.getOrderItems().stream()
                .map(oi -> new ItemOrderDto(oi))
                .collect(Collectors.toList());
    }
}
