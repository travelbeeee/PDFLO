package travelbeeee.PDFLO.web.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CommentForm {
    private String comment;
    private Double score;

    public CommentForm(String comment, Double score) {
        this.comment = comment;
        this.score = score;
    }
}
