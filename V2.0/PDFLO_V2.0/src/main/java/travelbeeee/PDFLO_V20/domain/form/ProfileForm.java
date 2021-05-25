package travelbeeee.PDFLO_V20.domain.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ProfileForm {
    @NotNull
    MultipartFile profile;
}
