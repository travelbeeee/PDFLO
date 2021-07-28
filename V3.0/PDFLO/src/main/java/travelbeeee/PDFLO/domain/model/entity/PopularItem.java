package travelbeeee.PDFLO.domain.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
public class PopularItem {
    @Id @GeneratedValue
    @Column(name = "popular_item_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Double score;
    private Double commentAvg;
    private Integer commentCnt;
    private Integer orderCnt;

    public PopularItem(Item item, Double score, Double commentAvg, Integer commentCnt, Integer orderCnt) {
        this.item = item;
        this.score = score;
        this.commentAvg = commentAvg;
        this.commentCnt = commentCnt;
        this.orderCnt = orderCnt;
    }
}
