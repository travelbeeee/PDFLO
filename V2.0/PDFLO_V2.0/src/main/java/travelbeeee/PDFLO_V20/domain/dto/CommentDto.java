package travelbeeee.PDFLO_V20.domain.dto;

import lombok.Data;
import travelbeeee.PDFLO_V20.domain.entity.Comment;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    String username;
    String comment;
    Double score;
    LocalDateTime createdDate;

    public CommentDto(Comment comment) {
        this.username = comment.getMember().getUsername();
        this.comment = comment.getComment();
        this.score = comment.getScore();
        this.createdDate = comment.getCreatedDate();
    }
}
