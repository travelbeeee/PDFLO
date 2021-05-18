package travelbeeee.PDFLOpjt.inputform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

// 구매자가 후기 남길 때 사용하는 form
@Getter @Setter @ToString
public class CommentForm {
    @NotNull
    String comments;
    @NotNull
    int score;
}


