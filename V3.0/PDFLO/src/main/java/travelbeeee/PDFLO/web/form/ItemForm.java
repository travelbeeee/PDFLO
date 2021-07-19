package travelbeeee.PDFLO.web.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 게시물 등록
 * 제목 / 내용 / 가격 / 썸네일이미지파일 / PDF파일 을 입력받는다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemForm {
    @NotEmpty(message = "제목을 입력해주세요.")
    String title;
    @NotEmpty(message = "내용을 입력해주세요.")
    String content;
    @NotNull(message = "가격을 입력해주세요.")
    Integer price;
    MultipartFile thumbnailFile;
    MultipartFile pdfFile;
}
