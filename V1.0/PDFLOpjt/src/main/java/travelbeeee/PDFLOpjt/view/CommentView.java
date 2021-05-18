package travelbeeee.PDFLOpjt.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @Setter @ToString
public class CommentView {
    int commentId;
    String username;
    LocalDate localdate;
    String comments;
    int score;
}
