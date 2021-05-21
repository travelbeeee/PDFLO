package travelbeeee.PDFLO_V20.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ProfileDto {
    @NotNull
    MultipartFile profile;
}
