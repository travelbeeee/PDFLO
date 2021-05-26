package travelbeeee.PDFLO_V20.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import travelbeeee.PDFLO_V20.domain.FileInformation;
import travelbeeee.PDFLO_V20.domain.entity.Item;

import java.time.LocalDateTime;

/**
 * Main 페이지에 뿌리는 ItemDto
 * Main 페이지에는 상품의 아이디 / 제목 / 작성자 / 작성날짜 / 썸네일위치 를 뿌려주자.
 */
@Data
@AllArgsConstructor
public class ItemMainDto {
    Long itemId;
    String title;
    String username;
    LocalDateTime createdDate;
    String thumbnailLocation;

    public ItemMainDto(Item item, String rootLocation) {
        this.itemId = item.getId();
        this.title = item.getTitle();
        this.username = item.getMember().getUsername();
        this.createdDate = item.getCreatedDate();
        FileInformation thumbnailFileInfo = item.getThumbnail().getFileInfo();
        this.thumbnailLocation = rootLocation + thumbnailFileInfo.getLocation() + "/resized-"
                + thumbnailFileInfo.getSaltedFileName() + thumbnailFileInfo.getExtension();
    }
}
