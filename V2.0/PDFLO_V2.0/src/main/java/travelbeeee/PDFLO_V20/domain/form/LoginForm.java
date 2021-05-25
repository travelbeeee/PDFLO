package travelbeeee.PDFLO_V20.domain.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @NotNull(message = "아이디를 양식에 맞게 입력해주세요.")
    @Pattern(regexp = "^(?=.*[0-9])[a-zA-z]{1}[a-zA-Z0-9]{4,14}$",
            message = "아이디는 영어로 시작하고, 5 ~ 15자의 영어/숫자로 이루어져야합니다.")
    private String username;

    @NotNull(message = "비밀번호를 양식에 맞게 입력해주세요.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%])[a-zA-z]{1}[a-zA-Z0-9!@#$%]{7,19}$",
            message = "비밀번호는 영어로 시작하고, 8 ~ 20자의 영어/숫자/특수문자(!@#$%)로 이루어져야합니다.")
    private String password;
}
