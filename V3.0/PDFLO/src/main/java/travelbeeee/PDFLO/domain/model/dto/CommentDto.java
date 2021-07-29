package travelbeeee.PDFLO.domain.model.dto;

import lombok.Data;
import travelbeeee.PDFLO.domain.model.entity.Comment;

import java.time.format.DateTimeFormatter;

@Data
public class CommentDto {
    Long memberId;
    Long commentId;
    String username;
    String comment;
    Double score;
    String createdDate;

    public CommentDto(Comment comment) {
        this.username = comment.getMember().getUsername();
        this.comment = comment.getComment();
        this.score = comment.getScore();
        this.createdDate = comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
    }
}
