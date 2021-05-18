package travelbeeee.PDFLO_V20.domain.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import travelbeeee.PDFLO_V20.domain.BaseEntity;
import travelbeeee.PDFLO_V20.domain.FileInformation;

import javax.persistence.*;

@Entity
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
}
