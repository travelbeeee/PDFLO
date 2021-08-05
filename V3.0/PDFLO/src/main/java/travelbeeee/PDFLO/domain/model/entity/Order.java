package travelbeeee.PDFLO.domain.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import travelbeeee.PDFLO.domain.model.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "ORDERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Order extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Integer totalPrice;

    @OneToMany(mappedBy = "order")
    List<OrderItem> orderItems = new ArrayList<>();

    public Order(Member member, Integer totalPrice) {
        this.member = member;
        this.totalPrice = totalPrice;
    }
}
