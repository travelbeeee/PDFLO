package travelbeeee.PDFLO_V20.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import travelbeeee.PDFLO_V20.domain.entity.Cart;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
