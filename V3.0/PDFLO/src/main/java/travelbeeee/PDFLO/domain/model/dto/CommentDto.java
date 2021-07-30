package travelbeeee.PDFLO.domain.model.dto;

import lombok.Data;
import travelbeeee.PDFLO.domain.model.entity.Comment;

import java.time.format.DateTimeFormatter;

@Data
public class CommentDto {
    Long memberId;
    Long commentId;
    Long recommentId = null;
    String username;
    String comment;
    Double score;
    String createdDate;
    String recomment = null;
    String recommentCreatedDate = null;

    public CommentDto(Comment comment) {
        this.username = comment.getMember().getUsername();
        this.comment = comment.getComment();
        this.score = comment.getScore();
        this.createdDate = comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        if(comment.getRecomment() != null){
            this.recommentId = comment.getRecomment().getId();
            this.recomment = comment.getRecomment().getComment();
            this.recommentCreatedDate = comment.getRecomment().getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
        }
    }
}
