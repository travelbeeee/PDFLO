package travelbeeee.PDFLO_V20.domain.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import travelbeeee.PDFLO_V20.domain.BaseEntity;
import travelbeeee.PDFLO_V20.domain.enumType.PointType;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "point_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Integer amount;
    private PointType type;

    public PointHistory(Member member, Integer amount, PointType type) {
        this.member = member;
        this.amount = amount;
        this.type = type;
    }
}
