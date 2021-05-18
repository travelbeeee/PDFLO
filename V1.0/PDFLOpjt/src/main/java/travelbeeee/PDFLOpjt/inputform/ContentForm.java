package travelbeeee.PDFLOpjt.inputform;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

// 판매자가 글 작성할 때 사용되는 form
@Getter @Setter @ToString
public class ContentForm {
    @NotNull
    String title;
    @NotNull
    String content;
    @NotNull
    int price;
    @NotNull
    MultipartFile thumbnailFile;
    @NotNull
    MultipartFile pdfFile;
}
