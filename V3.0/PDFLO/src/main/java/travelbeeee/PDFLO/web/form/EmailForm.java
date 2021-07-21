package travelbeeee.PDFLO.web.form;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class EmailForm {
    @NotEmpty(message = "이메일을 양식에 맞게 입력해주세요.")
    @Email(message = "제대로 된 이메일을 입력해주세요.")
    private String email;
}
