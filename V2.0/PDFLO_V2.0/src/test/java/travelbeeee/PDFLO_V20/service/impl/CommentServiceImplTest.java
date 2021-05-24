package travelbeeee.PDFLO_V20.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO_V20.domain.entity.*;
import travelbeeee.PDFLO_V20.dto.CommentDto;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.repository.*;
import travelbeeee.PDFLO_V20.service.CommentService;

import javax.mail.FetchProfile;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class CommentServiceImplTest {
    @Autowired
    CommentService commentService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 댓글_업로드_정상() throws Exception{
        // given
        Member member = new Member("member1", null, null, null, null, null);
        memberRepository.save(member);

        Item item = new Item(member, "item1", null, null, null, null);
        itemRepository.save(item);

        Order order = new Order(member);
        orderRepository.save(order);

        OrderItem orderItem = new OrderItem(order, item, 10000);
        orderItemRepository.save(orderItem);

        CommentDto newCommentDto = new CommentDto("comment", 4.0D);
        commentService.uploadComment(member.getId(), item.getId(), newCommentDto);

        em.flush();
        em.clear();

        List<Comment> comments = commentRepository.findAll();
        Assertions.assertThat(comments.size()).isEqualTo(1);
        Assertions.assertThat(comments.get(0).getComment()).isEqualTo("comment");
        Assertions.assertThat(comments.get(0).getScore()).isEqualTo(4.0D);
    }

    @Test
    public void 댓글_업로드_회원X() throws Exception {
        // given
        Member member1 = new Member("member1", null, null, null, null, null);
        memberRepository.save(member1);

        Item item1 = new Item(member1, "item1", null, null, null, null);
        itemRepository.save(item1);

        CommentDto comment1 = new CommentDto("comment1", 4.5D);
        // when --> then PDFLOException Error

        PDFLOException pdfloException = assertThrows(
                PDFLOException.class, () -> commentService.uploadComment(member1.getId() + 100L, item1.getId(), comment1)
        );

        assertEquals(pdfloException.getReturnCode().getMessage(), "없는 회원입니다.");
    }

    @Test
    public void 댓글_업로드_아이템X() throws Exception {
        // given
        Member member1 = new Member("member1", null, null, null, null, null);
        memberRepository.save(member1);

        Item item1 = new Item(member1, "item1", null, null, null, null);
        itemRepository.save(item1);

        CommentDto comment1 = new CommentDto("comment1", 4.5D);
        // when --> then PDFLOException Error

        PDFLOException pdfloException = assertThrows(
                PDFLOException.class, () -> commentService.uploadComment(member1.getId(), item1.getId() + 100L, comment1)
        );

        assertEquals(pdfloException.getReturnCode().getMessage(), "없는 상품입니다.");
    }

    @Test
    public void 댓글_업로드_구매한적없는회원() throws Exception {
        // given
        Member member1 = new Member("member1", null, null, null, null, null);
        memberRepository.save(member1);

        Item item1 = new Item(member1, "item1", null, null, null, null);
        itemRepository.save(item1);

        CommentDto comment1 = new CommentDto("comment1", 4.5D);
        // when --> then PDFLOException Error

        PDFLOException pdfloException = assertThrows(
                PDFLOException.class, () -> commentService.uploadComment(member1.getId(), item1.getId(), comment1)
        );

        assertEquals(pdfloException.getReturnCode().getMessage(), "구매자만 후기를 남길 수 있습니다.");
    }

    @Test
    public void 댓글_업로드_이미후기를작성한회원() throws Exception {
        // given
        Member member = new Member("member1", null, null, null, null, null);
        memberRepository.save(member);

        Item item = new Item(member, "item1", null, null, null, null);
        itemRepository.save(item);

        Order order = new Order(member);
        orderRepository.save(order);

        OrderItem orderItem = new OrderItem(order, item, 10000);
        orderItemRepository.save(orderItem);

        Comment comment = new Comment(member, item, "comment1", 4.5D);
        commentRepository.save(comment);

        em.flush();
        em.clear();

        CommentDto newCommentDto = new CommentDto("newComment", 4.0D);
        PDFLOException pdfloException = assertThrows(
                PDFLOException.class, () -> commentService.uploadComment(member.getId(), item.getId(), newCommentDto)
        );

        assertEquals(pdfloException.getReturnCode().getMessage(), "이미 후기를 작성하셨습니다.");
    }

    @Test
    public void 댓글_삭제_정상() throws Exception{
        // given
        Member member = new Member("member1", null, null, null, null, null);
        memberRepository.save(member);

        Item item = new Item(member, "item1", null, null, null, null);
        itemRepository.save(item);

        Order order = new Order(member);
        orderRepository.save(order);

        OrderItem orderItem = new OrderItem(order, item, 10000);
        orderItemRepository.save(orderItem);

        Comment comment = new Comment(member, item, "comment", 4.0D);
        commentRepository.save(comment);

        // when
        commentService.deleteComment(member.getId(), comment.getId());

        // then
        List<Comment> comments = commentRepository.findAll();
        Assertions.assertThat(comments.size()).isEqualTo(0);
        Assertions.assertThat(comments.isEmpty()).isEqualTo(true);
    }

    @Test
    public void 댓글_수정_정상() throws Exception{
        // given
        Member member = new Member("member1", null, null, null, null, null);
        memberRepository.save(member);

        Item item = new Item(member, "item1", null, null, null, null);
        itemRepository.save(item);

        Order order = new Order(member);
        orderRepository.save(order);

        OrderItem orderItem = new OrderItem(order, item, 10000);
        orderItemRepository.save(orderItem);

        Comment comment = new Comment(member, item, "comment", 4.0D);
        commentRepository.save(comment);

        // when
        commentService.modifyComment(member.getId(), comment.getId(), new CommentDto("newComment", 4.5D));

        // then
        Optional<Comment> findComment = commentRepository.findById(comment.getId());
        Assertions.assertThat(findComment.isEmpty()).isFalse();
        Comment comment1 = findComment.get();
        Assertions.assertThat(comment1.getComment()).isEqualTo("newComment");
        Assertions.assertThat(comment1.getScore()).isEqualTo(4.5D);
    }

}