package travelbeeee.PDFLO.domain.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopularItem {
    @Id @GeneratedValue
    @Column(name = "popular_item_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Double score;

    public PopularItem(Item item, Double score) {
        this.item = item;
        this.score = score;
    }
}
