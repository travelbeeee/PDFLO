package travelbeeee.PDFLO.domain.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import travelbeeee.PDFLO.domain.model.BaseEntity;
import travelbeeee.PDFLO.domain.model.FileInformation;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "profile_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private FileInformation fileInfo;

    public Profile(Member member, FileInformation fileInfo) {
        this.member = member;
        this.fileInfo = fileInfo;
    }
}
