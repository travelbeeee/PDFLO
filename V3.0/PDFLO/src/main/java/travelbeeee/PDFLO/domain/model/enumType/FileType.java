package travelbeeee.PDFLO.domain.model.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileType {
    PDF("pdf/", false, 0, 0),
    THUMBNAIL("thumbnail/", true, 400, 400),
    PROFILE("profile/", true, 40, 40);

    private String typePath;
    private Boolean needResized;
    private Integer resizeWidth;
    private Integer resizeHeight;
}
