package travelbeeee.PDFLO_V20.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.LifecycleState;
import travelbeeee.PDFLO_V20.domain.FileInformation;
import travelbeeee.PDFLO_V20.domain.entity.Comment;
import travelbeeee.PDFLO_V20.domain.entity.Item;
import travelbeeee.PDFLO_V20.repository.CommentRepository;

import java.time.LocalDateTime;
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
    LocalDateTime createdDate;
    String thumbnailLocation;
    String pdfLocation;
    List<CommentDto> comments = new ArrayList<>();

    public ItemDetailDto(Item item) {
        this.itemId = item.getId();
        this.title = item.getTitle();
        this.content = item.getContent();
        this.username = item.getMember().getUsername();
        this.price = item.getPrice();
        this.createdDate = item.getCreatedDate();
        FileInformation thumbnailFileInfo = item.getThumbnail().getFileInfo();
        FileInformation pdfFileInfo = item.getPdf().getFileInfo();

        this.thumbnailLocation = thumbnailFileInfo.getLocation() + "/" +
                thumbnailFileInfo.getSaltedFileName() + thumbnailFileInfo.getExtension();
        this.pdfLocation = pdfFileInfo.getLocation() + "/" +
                pdfFileInfo.getSaltedFileName() + pdfFileInfo.getExtension();

        List<Comment> comments = item.getComments();
        this.comments = comments.stream()
                .map(c -> new CommentDto(c))
                .collect(Collectors.toList());
    }
}