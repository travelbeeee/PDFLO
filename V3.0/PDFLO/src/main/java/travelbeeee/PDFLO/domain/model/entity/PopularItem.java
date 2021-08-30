package travelbeeee.PDFLO.domain.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import travelbeeee.PDFLO.domain.model.BaseEntity;

import javax.persistence.*;

/**
 *  create table popular_item(
 *   popular_item_id bigint not null auto_increment,
 *   item_id bigint not null,
 *   score double,
 *   comment_avg double,
 *   comment_cnt int,
 *   order_cnt int,
 *   primary key(id)
 *  )
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PopularItem extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "popular_item_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private Double score;

    public PopularItem(Item item, Double score) {
        this.item = item;
        this.score = score;
    }

    public void updatePopularity(Double score) {
        this.score = score;
    }
}
