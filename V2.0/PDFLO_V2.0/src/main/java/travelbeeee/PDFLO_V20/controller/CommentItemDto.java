package travelbeeee.PDFLO_V20.controller;

import lombok.Data;
import travelbeeee.PDFLO_V20.domain.entity.Comment;

@Data
public class CommentItemDto {
    private Long commentId;
    private Long itemId;
    private String title;
    private String comment;
    private Double score;

    public CommentItemDto(Comment c) {
        this.commentId = c.getId();
        this.itemId = c.getItem().getId();
        this.title = c.getItem().getTitle();
        this.comment = c.getComment();
        this.score = c.getScore();
    }
}
