package travelbeeee.PDFLO_V20.domain.form;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CommentForm {
    @NotNull
    private String comment;

    @NotNull
    private Double score;

    public CommentForm(String comment, Double score) {
        this.comment = comment;
        this.score = score;
    }
}
