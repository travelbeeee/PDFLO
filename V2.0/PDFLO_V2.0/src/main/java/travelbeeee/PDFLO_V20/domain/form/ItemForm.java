package travelbeeee.PDFLO_V20.domain.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 게시물 등록
 * 제목 / 내용 / 가격 / 썸네일이미지파일 / PDF파일 을 입력받는다.
 */
@Data
@AllArgsConstructor
public class ItemForm {
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
