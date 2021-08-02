package travelbeeee.PDFLO.web.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class PointForm {
    @NotNull
    @Min(value = 1000)
    @Max(value = 1000000)
    Integer point;

}
