package travelbeeee.PDFLO.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import travelbeeee.PDFLO.domain.model.FileInformation;
import travelbeeee.PDFLO.domain.model.entity.Item;

import java.time.format.DateTimeFormatter;

/**
 * Main 페이지에 뿌리는 ItemDto
 * Main 페이지에는 제목 / 작성자 / 작성날짜 / 썸네일위치 를 뿌려주자.
 * --> detail 페이지로 넘어가기 위해 상품번호도 넘겨주자.
 */
@Data
@AllArgsConstructor
public class ItemViewDto {
    Long itemId;
    String title;
    String username;
    String createdDate;
    String thumbnailLocation;
    String thumbnailFileName;

    public ItemViewDto(Item item) {
        this.itemId = item.getId();
        this.title = item.getTitle();
        this.username = item.getMember().getUsername();
        this.createdDate = item.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
        FileInformation thumbnailFileInfo = item.getThumbnail().getFileInfo();
        this.thumbnailLocation = thumbnailFileInfo.getLocation();
        this.thumbnailFileName = thumbnailFileInfo.getSaltedFileName();
    }
}
