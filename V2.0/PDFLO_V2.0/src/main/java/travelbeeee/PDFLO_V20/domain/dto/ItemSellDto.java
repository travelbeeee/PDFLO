package travelbeeee.PDFLO_V20.domain.dto;

import lombok.Data;
import travelbeeee.PDFLO_V20.domain.FileInformation;
import travelbeeee.PDFLO_V20.domain.entity.Item;

import java.time.format.DateTimeFormatter;

@Data
public class ItemSellDto {
    private String title;
    private String thumbnailLocation;
    private Integer price;
    private String createdDate;
    private Long itemId;

    public ItemSellDto(Item i) {
        this.title = i.getTitle();
        FileInformation thumbnailInfo = i.getThumbnail().getFileInfo();
        this.thumbnailLocation = thumbnailInfo.getLocation() + "/" + thumbnailInfo.getSaltedFileName() + thumbnailInfo.getExtension();
        this.price = i.getPrice();
        this.createdDate = i.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
        this.itemId = i.getId();
    }
}
