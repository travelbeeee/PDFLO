package travelbeeee.PDFLO.domain.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import travelbeeee.PDFLO.domain.model.BaseEntity;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recomment extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "recomment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String comment;

    public Recomment(Member member, String comment) {
        this.id = id;
        this.member = member;
        this.comment = comment;
    }
}
