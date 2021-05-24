package travelbeeee.PDFLO_V20.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import travelbeeee.PDFLO_V20.domain.BaseEntity;
import travelbeeee.PDFLO_V20.dto.CommentDto;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private String comment;
    private Double score;

    public Comment(Member member, Item item, String comment, Double score) {
        this.member = member;
        this.item = item;
        this.comment = comment;
        this.score = score;
    }

    public void modifyComment(CommentDto commentDto) {
        this.comment = commentDto.getComment();
        this.score = commentDto.getScore();
    }
}
