package travelbeeee.PDFLO_V20.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import travelbeeee.PDFLO_V20.domain.FileInformation;
import travelbeeee.PDFLO_V20.domain.entity.Item;

import java.time.LocalDate;
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

    public ItemViewDto(Item item) {
        this.itemId = item.getId();
        this.title = item.getTitle();
        this.username = item.getMember().getUsername();
        this.createdDate = item.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
        FileInformation thumbnailFileInfo = item.getThumbnail().getFileInfo();
        this.thumbnailLocation = thumbnailFileInfo.getLocation() + "/resized-"
                + thumbnailFileInfo.getSaltedFileName() + thumbnailFileInfo.getExtension();
    }

}
