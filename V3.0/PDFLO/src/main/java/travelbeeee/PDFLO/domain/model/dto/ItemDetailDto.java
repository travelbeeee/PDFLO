package travelbeeee.PDFLO.domain.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import travelbeeee.PDFLO.domain.model.FileInformation;
import travelbeeee.PDFLO.domain.model.entity.Comment;
import travelbeeee.PDFLO.domain.model.entity.Item;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Item Detail 페이지에 뿌리는 Dto
 *  썸네일 / 상품 제목 / 내용 / 작성자 / 작성날짜 / 댓글 를 View로 넘겨주자.
 *  --> 삭제 / 수정 을 위해 상품Id도 넘겨주자.
 */
@Data
@Getter
@NoArgsConstructor
public class ItemDetailDto {
    Long itemId;
    String title;
    String content;
    String username;
    Integer price;
    String createdDate;
    String thumbnailLocation;
    String thumbnailFileName;
    String pdfLocation;
    String pdfFileName;
    List<CommentDto> comments = new ArrayList<>();

    public ItemDetailDto(Item item) {
        this.itemId = item.getId();
        this.title = item.getTitle();
        this.content = item.getContent();
        this.username = item.getMember().getUsername();
        this.price = item.getPrice();
        this.createdDate = item.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));

        FileInformation thumbnailFileInfo = item.getThumbnail().getFileInfo();
        FileInformation pdfFileInfo = item.getPdf().getFileInfo();

        this.thumbnailLocation = thumbnailFileInfo.getLocation();
        this.thumbnailFileName = thumbnailFileInfo.getSaltedFileName();
        this.pdfLocation = pdfFileInfo.getLocation();
        this.pdfFileName = pdfFileInfo.getSaltedFileName();
        List<Comment> comments = item.getComments();
        this.comments = comments.stream()
                .map(c -> new CommentDto(c))
                .collect(Collectors.toList());
    }


}