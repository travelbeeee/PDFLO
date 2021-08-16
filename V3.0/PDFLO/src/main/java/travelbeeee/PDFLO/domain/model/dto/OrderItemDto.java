package travelbeeee.PDFLO.domain.model.dto;

import lombok.Data;
import travelbeeee.PDFLO.domain.model.entity.OrderItem;
import travelbeeee.PDFLO.domain.model.entity.Thumbnail;

import java.time.format.DateTimeFormatter;

@Data
public class OrderItemDto {
    private Long itemId;
    private String title;
    private Integer orderPrice;
    private String thumbnail;
    private String createdDate;

    public OrderItemDto(OrderItem oi) {
        this.itemId = oi.getItem().getId();
        this.title = oi.getItem().getTitle();
        this.orderPrice = oi.getOrderPrice();
        this.thumbnail = oi.getItem().getThumbnail().getFileInfo().getLocation() +
                "resized-" + oi.getItem().getThumbnail().getFileInfo().getSaltedFileName();
        this.createdDate = oi.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
    }
}
