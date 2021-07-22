package travelbeeee.PDFLO.web.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 로그인
 * 아이디 / 비밀번호를 입력받는다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {
    @NotEmpty
    @Pattern(regexp = "^(?=.*[0-9])[a-zA-z]{1}[a-zA-Z0-9]{4,14}$")
    private String username;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%])[a-zA-z]{1}[a-zA-Z0-9!@#$%]{7,19}$")
    private String password;
}
