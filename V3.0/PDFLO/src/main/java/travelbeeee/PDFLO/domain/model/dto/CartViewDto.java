package travelbeeee.PDFLO.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import travelbeeee.PDFLO.domain.model.entity.Cart;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
public class CartViewDto {
    Long cartId;
    Long itemId;
    String title;
    String createdDate;

    public CartViewDto(Cart c) {
        this.cartId = c.getId();
        this.itemId = c.getItem().getId();
        this.title = c.getItem().getTitle();
        this.createdDate = c.getItem().getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
    }
}
