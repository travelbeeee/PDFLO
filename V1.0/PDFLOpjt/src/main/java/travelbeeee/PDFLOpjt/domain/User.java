package travelbeeee.PDFLOpjt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class User {
    int userId;
    String username;
    String userpwd;
    String email;
    String salt;
    String auth;
}
