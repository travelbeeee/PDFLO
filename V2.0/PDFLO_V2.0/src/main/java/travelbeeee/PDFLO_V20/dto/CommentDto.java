package travelbeeee.PDFLO_V20.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommentDto {
    @NotNull
    private String comment;

    @NotNull
    private Double score;

    public CommentDto(String comment, Double score) {
        this.comment = comment;
        this.score = score;
    }
}
