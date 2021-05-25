package travelbeeee.PDFLO_V20.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelbeeee.PDFLO_V20.domain.entity.*;
import travelbeeee.PDFLO_V20.domain.form.CommentForm;
import travelbeeee.PDFLO_V20.exception.ErrorCode;
import travelbeeee.PDFLO_V20.exception.PDFLOException;
import travelbeeee.PDFLO_V20.repository.*;
import travelbeeee.PDFLO_V20.service.CommentService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    /**
     * 1) 해당 회원이 존재하는지 확인
     * 2) 해당 아이템이 존재하는지 확인
     * 3) 해당 회원이 해당 아이템을 구매했는지 확인
     * 4) 해당 회원이 해당 아이템에 후기를 이미 남겼는지 확인
     */
    @Transactional
    @Override
    public void uploadComment(Long memberId, Long itemId, CommentForm commentForm) throws PDFLOException {
        // 1)
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);

        // 2)
        Optional<Item> findItem = itemRepository.findById(itemId);
        if(findItem.isEmpty()) throw new PDFLOException(ErrorCode.ITEM_NO_EXIST);

        // 3)
        Member member = findMember.get();
        Item item = findItem.get();
        Optional<OrderItem> findOrderItem = orderItemRepository.findByMemberAndItem(member, item);
        if(findOrderItem.isEmpty()) throw new PDFLOException(ErrorCode.COMMENT_NO_PERMISSION_BUYING);

        // 4)
        Optional<Comment> findComment = commentRepository.findByMemberIdAndItemId(memberId, itemId);
        if(!findComment.isEmpty()) throw new PDFLOException(ErrorCode.COMMENT_ALREADY_WRITTEN);

        Comment comment = new Comment(member, item, commentForm.getComment(), commentForm.getScore());
        commentRepository.save(comment);
    }

    /**
     * 1) 해당 회원이 존재하는지 확인
     * 2) 해당 후기가 존재하는지 확인
     * 3) 해당 회원이 해당 후기를 작성했는지 확인
     * 4) 해당 후기를 꺼내와서 수정
     */
    @Transactional
    @Override
    public void modifyComment(Long memberId, Long commentId, CommentForm commentForm) throws PDFLOException {
        // 1)
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);

        // 2)
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()) throw new PDFLOException(ErrorCode.COMMENT_NO_EXIST);

        // 3)
        Comment comment = findComment.get();
        if(comment.getMember().getId() != memberId) throw new PDFLOException(ErrorCode.COMMENT_NO_PERMISSION_WRITER);

        // 4)
        comment.modifyComment(commentForm);
    }

    /**
     * 1) 해당 회원이 존재하는지 확인
     * 2) 해당 후기가 존재하는지 확인
     * 3) 해당 회원이 해당 후기를 작성했는지 확인
     */
    @Transactional
    @Override
    public void deleteComment(Long memberId, Long commentId) throws PDFLOException {
        // 1)
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isEmpty()) throw new PDFLOException(ErrorCode.MEMBER_NO_EXIST);

        // 2)
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()) throw new PDFLOException(ErrorCode.COMMENT_NO_EXIST);

        // 3)
        Comment comment = findComment.get();
        if(comment.getMember().getId() != memberId) throw new PDFLOException(ErrorCode.COMMENT_NO_PERMISSION_WRITER);

        commentRepository.delete(comment);
    }

    @Override
    public List<Comment> findAllByItem(Long itemId) {
        return commentRepository.findAllByItem(itemId);
    }

}
