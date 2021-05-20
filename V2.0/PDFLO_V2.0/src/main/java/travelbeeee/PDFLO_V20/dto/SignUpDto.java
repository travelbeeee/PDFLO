package travelbeeee.PDFLO_V20.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 회원가입
 * 아이디 / 비밀번호 / 이메일을 입력받는다.
 */
@Data
@AllArgsConstructor
public class SignUpDto {
    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])[a-zA-z]{1}[a-zA-Z0-9]{4,14}$",
            message = "아이디는 영어로 시작하고, 5 ~ 15자의 영어/숫자로 이루어져야합니다.")
    private String username;

    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%])[a-zA-z]{1}[a-zA-Z0-9!@#$%]{7,19}$",
            message = "비밀번호는 영어로 시작하고, 8 ~ 20자의 영어/숫자/특수문자(!@#$%)로 이루어져야합니다.")
    private String password;

    @NotNull
    @Email
    private String email;
}
