package travelbeeee.PDFLOpjt.view;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * View에 넘겨주는 Content는
 * ContentId
 * username(누가)
 * localdate(언제)
 * thumbnail(썸네일파일위치)
 * title(어떤제목의)
 */
@Getter @Setter @ToString
public class ContentView {
    int contentId;
    String username;
    LocalDate localdate;
    String thumbnailLocation;
    String title;
}
