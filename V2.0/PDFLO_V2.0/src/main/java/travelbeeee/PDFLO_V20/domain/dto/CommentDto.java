package travelbeeee.PDFLO_V20.domain.dto;

import lombok.Data;
import travelbeeee.PDFLO_V20.domain.entity.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class CommentDto {
    String username;
    String comment;
    Double score;
    String createdDate;
    Long commentId;

    public CommentDto(Comment comment) {
        this.username = comment.getMember().getUsername();
        this.comment = comment.getComment();
        this.score = comment.getScore();
        this.createdDate = comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
        this.commentId = comment.getId();
    }
}
