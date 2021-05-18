package travelbeeee.PDFLOpjt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Profile {
    int userId;
    int profileId;
    String originFileName;
    String saltedFileName;
    String location;
}
