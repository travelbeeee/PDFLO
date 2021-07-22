package travelbeeee.PDFLO.domain.model.dto;

import lombok.Data;
import travelbeeee.PDFLO.domain.model.FileInformation;
import travelbeeee.PDFLO.domain.model.entity.Item;
import travelbeeee.PDFLO.domain.model.enumType.ItemType;

import java.time.format.DateTimeFormatter;

@Data
public class ItemSellDto {
    private String title;
    private String thumbnail;
    private Integer price;
    private String createdDate;
    private Long itemId;
    private ItemType type;

    public ItemSellDto(Item i) {
        this.title = i.getTitle();
        FileInformation thumbnailInfo = i.getThumbnail().getFileInfo();
        this.thumbnail = thumbnailInfo.getLocation() + thumbnailInfo.getSaltedFileName();
        this.price = i.getPrice();
        this.createdDate = i.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
        this.itemId = i.getId();
        this.type = i.getType();
    }
}
