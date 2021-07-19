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
@ToString
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

    @Enumerated(EnumType.STRING)
    private ItemType type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_id")
    private Thumbnail thumbnail;

    @OneToOne(fetch = FetchType.LAZY)
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
    }

    public Item(Member member, String title, String content, Integer price, Thumbnail thumbnail, Pdf pdf) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.price = price;
        this.thumbnail = thumbnail;
        this.pdf = pdf;
        this.type = ItemType.SELL;
    }

    public void changeItem(String title, String content, Integer price, Thumbnail thumbnail, Pdf pdf){
        this.title = title;
        this.content = content;
        this.price = price;
        this.thumbnail = thumbnail;
        this.pdf = pdf;
    }

    public void stopSell() {
        this.type = ItemType.STOP;
    }

    public void reSell(){
        this.type = ItemType.SELL;
    }
}
