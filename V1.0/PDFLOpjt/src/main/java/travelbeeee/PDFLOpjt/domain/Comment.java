package travelbeeee.PDFLOpjt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter @ToString @Setter
public class Comment {
    int userId;
    int contentId;
    int commentId;
    LocalDate localdate;
    String comments;
    int score;
}
