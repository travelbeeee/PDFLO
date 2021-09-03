package travelbeeee.PDFLO.domain.model.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import travelbeeee.PDFLO.domain.model.FileInformation;
import travelbeeee.PDFLO.domain.model.entity.Item;
import travelbeeee.PDFLO.domain.model.entity.PopularItem;
import travelbeeee.PDFLO.domain.model.entity.Thumbnail;
import travelbeeee.PDFLO.domain.model.enumType.ItemType;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Main 페이지에 뿌리는 ItemDto
 * CommentTable과 ItemTable을 참조해 아이템별로
 * 상품번호, 상품제목, 판매자, 상품등록날짜, 썸네일, 평점, 후기갯수 가져오기위한 Dto
 */
@Getter @Setter
@ToString
@Slf4j
public class ItemDto {
    Long itemId;
    String title;
    String createdDate;
    String commentAvg;
    Long commentCnt;
    Long orderCnt;
    String thumbnail;
    ItemType itemType;

    public ItemDto(PopularItem pi) {
        this.itemId = pi.getItem().getId();
        this.title = pi.getItem().getTitle();
        this.createdDate = pi.getItem().getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
        DecimalFormat df = new DecimalFormat("0.00");
        this.commentAvg = df.format(pi.getItem().getCommentAvg());
        this.commentCnt = pi.getItem().getCommentCnt();
        this.orderCnt = pi.getItem().getOrderCnt();
        this.thumbnail = pi.getItem().getThumbnail().getFileInfo().resizedFileReference();
        this.itemType = pi.getItem().getType();
    }
}
