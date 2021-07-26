package travelbeeee.PDFLO.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import travelbeeee.PDFLO.domain.model.FileInformation;
import travelbeeee.PDFLO.domain.model.entity.Item;
import travelbeeee.PDFLO.domain.model.entity.Thumbnail;

import java.time.format.DateTimeFormatter;

/**
 * Main 페이지에 뿌리는 ItemDto
 * CommentTable과 ItemTable을 참조해 아이템별로
 * 상품번호, 상품제목, 판매자, 상품등록날짜, 썸네일, 평점, 후기갯수 가져오기위한 Dto
 *
 * select avg(score) as avgScore, count(*) as commentCnt, comment.item_id, item.thumbnail_id, item.title, item.created_date, item.username
 * from comment right outer join item
 * on (comment.item_id= item.item_id)
 * group by (item.item_id);
 */
@Data
@AllArgsConstructor
@ToString(exclude = { "thumbnail" })
public class ItemViewDto {
    Long itemId;
    String title;
    String username;
    String createdDate;
    Thumbnail thumbnail;
    Double avgScore;
    Integer commentCnt;
}
