package travelbeeee.PDFLO.domain.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import travelbeeee.PDFLO.domain.model.BaseEntity;
import travelbeeee.PDFLO.domain.model.enumType.MemberType;

import javax.persistence.*;

@Entity
@Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String password;
    private String salt;
    private String email;

    @Enumerated(EnumType.STRING)
    private MemberType type;
    private Integer point;

    public Member(String username, String password, String salt, String email, MemberType type, Integer point) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.type = type;
        this.point = point;
    }

    public void changeType(MemberType type){
        this.type = type;
    }

    public void changePassword(String password){ this.password = password; }

    public void getPoint(Integer point){ this.point += point;}

    public void losePoint(Integer point) { this.point -= point;}
}
