package travelbeeee.PDFLO_V20.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import travelbeeee.PDFLO_V20.domain.entity.OrderItem;

@Data
@AllArgsConstructor
public class ItemOrderDto {
    Long itemId;
    String title;
    String content;
    Integer price;

    public ItemOrderDto(OrderItem oi) {
        this.itemId = oi.getItem().getId();
        this.title = oi.getItem().getTitle();
        this.content = oi.getItem().getContent();
        this.price = oi.getItem().getPrice();
    }
}
