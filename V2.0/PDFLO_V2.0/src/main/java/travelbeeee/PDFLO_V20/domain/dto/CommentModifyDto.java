package travelbeeee.PDFLO_V20.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import travelbeeee.PDFLO_V20.domain.entity.Comment;

@Data
@AllArgsConstructor
public class CommentModifyDto {
    String comment;
    Double score;
    Long commentId;

    public CommentModifyDto(Comment c){
        this.comment = c.getComment();
        this.score = c.getScore();
        this.commentId = c.getId();
    }
}
