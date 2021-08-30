package travelbeeee.PDFLO.domain.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import travelbeeee.PDFLO.domain.model.BaseEntity;
import travelbeeee.PDFLO.domain.model.enumType.ItemType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;
    private String content;
    private Integer price;
    private Long orderCnt;
    private Long commentCnt;
    private Double commentAvg;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "thumbnail_id")
    private Thumbnail thumbnail;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pdf_id")
    private Pdf pdf;

    @OneToMany(mappedBy = "item")
    private List<Comment> comments = new ArrayList<>();

    public Item(Member member, String title, String content, Integer price, Thumbnail thumbnail, Pdf pdf, ItemType type) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.price = price;
        this.thumbnail = thumbnail;
        this.pdf = pdf;
        this.type = type;
        this.orderCnt = 0L;
        this.commentCnt = 0L;
        this.commentAvg = 0.0;
    }

    public Item(Member member, String title, String content, Integer price, Thumbnail thumbnail, Pdf pdf) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.price = price;
        this.thumbnail = thumbnail;
        this.pdf = pdf;
        this.type = ItemType.SELL;
        this.orderCnt = 0L;
        this.commentCnt = 0L;
        this.commentAvg = 0.0;
    }

    public void changeItem(String title, String content, Integer price){
        this.title = title;
        this.content = content;
        this.price = price;
    }

    public void uploadComment(Double commentScore){
        this.commentAvg = ((this.commentAvg * this.commentCnt) + commentScore) / (this.commentCnt + 1);
        this.commentCnt++;
    }

    public void sellItem(){
        this.orderCnt++;
    }

    public void stopSell() {
        this.type = ItemType.STOP;
    }

    public void reSell(){
        this.type = ItemType.SELL;
    }
}
